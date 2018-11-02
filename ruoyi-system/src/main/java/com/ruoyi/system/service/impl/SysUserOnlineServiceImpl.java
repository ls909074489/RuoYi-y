package com.ruoyi.system.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.common.enums.OnlineStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.mapper.SysUserOnlineMapper;

/**
 * 在线用户 服务层处理
 * 
 * @author ruoyi
 */
@Component
public class SysUserOnlineServiceImpl
{
    @Autowired
    private SysUserOnlineMapper userOnlineDao;
    @Autowired
    private RedisSessionDAO redisSessionDAO;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private RedisCacheManager redisCacheManager;

    /**
     * 通过会话序号查询信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineById(String sessionId)
    {
        return userOnlineDao.selectOnlineById(sessionId);
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public void deleteOnlineById(String sessionId)
    {
        SysUserOnline userOnline = selectOnlineById(sessionId);
        if (StringUtils.isNotNull(userOnline))
        {
            userOnlineDao.deleteOnlineById(sessionId);
        }
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    public void batchDeleteOnline(List<String> sessions)
    {
        for (String sessionId : sessions)
        {
            SysUserOnline userOnline = selectOnlineById(sessionId);
            if (StringUtils.isNotNull(userOnline))
            {
                userOnlineDao.deleteOnlineById(sessionId);
            }
        }
    }

    /**
     * 保存会话信息
     * 
     * @param online 会话信息
     */
    public void saveOnline(SysUserOnline online)
    {
        userOnlineDao.saveOnline(online);
    }

    /**
     * 查询会话集合
     * 
     * @param pageUtilEntity 分页参数
     */
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline)
    {
//        return userOnlineDao.selectUserOnlineList(userOnline);
    	
        // 因为我们是用redis实现了shiro的session的Dao,而且是采用了shiro+redis这个插件
        // 所以从spring容器中获取redisSessionDAO
        // 来获取session列表.
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        Iterator<Session> it = sessions.iterator();
        List<SysUserOnline> onlineUserList = new ArrayList<SysUserOnline>();
        // 遍历session
        while (it.hasNext()) {
            // 这是shiro已经存入session的
            // 现在直接取就是了
            Session session = it.next();
            //标记为已提出的不加入在线列表
            if(session!=null&&session.getAttribute("kickout") != null) {
                continue;
            }
            SysUserOnline onlineUser = getSessionBo(session);
            if(onlineUser!=null){
                /*用户名搜索*/
                if(StringUtils.isNotBlank(userOnline.getLoginName())){
                    if(onlineUser.getLoginName().contains(userOnline.getLoginName()) ){
                        onlineUserList.add(onlineUser);
                    }
                }else{
                    onlineUserList.add(onlineUser);
                }
            }
        }
//        int endIndex = (offset+limit) > onlineUserList.size() ? onlineUserList.size() : (offset+limit);
//        onlineUserList.subList(offset,endIndex),(long)onlineUserList.size());
        return onlineUserList;
    }
    
    private SysUserOnline getSessionBo(Session session){
        //获取session登录信息。
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if(null == obj){
            return null;
        }
        //确保是 SimplePrincipalCollection对象。
        if(obj instanceof SimplePrincipalCollection){
            SimplePrincipalCollection spc = (SimplePrincipalCollection)obj;
            obj = spc.getPrimaryPrincipal();
            if(null != obj && obj instanceof SysUser){
            	SysUser user = (SysUser) obj;
                //存储session + user 综合信息
            	SysUserOnline userBo = new SysUserOnline();
            	
            	/** 用户会话id */
               userBo.setSessionId((String)session.getId());
                /** 部门名称 */
               userBo.setDeptName(user.getDept().getDeptName());
                /** 登录名称 */
                userBo.setLoginName(user.getLoginName());
                /** 登录IP地址 */
                userBo.setIpaddr(user.getLoginIp());
                /** 登录地址 */
//                userBo.setLoginLocation(user.get);
                /** 浏览器类型 */
//                userBo.setBrowser(session.get);
                /** 操作系统 */
//                userBo.setOs(os);
                /** session创建时间 */
                userBo.setStartTimestamp(session.getStartTimestamp());
                /** session最后访问时间 */
                userBo.setLastAccessTime(session.getLastAccessTime());
                /** 超时时间，单位为分钟 */
                userBo.setExpireTime(session.getTimeout());
                /** 在线状态 */
                userBo.setStatus(OnlineStatus.on_line);
                return userBo;
            }
        }
        return null;
    }

    /**
     * 强退用户
     * 
     * @param sessionId 会话ID
     */
    public void forceLogout(String sessionId)
    {
        userOnlineDao.deleteOnlineById(sessionId);
    }

    /**
     * 查询会话集合
     * 
     * @param online 会话信息
     */
    public List<SysUserOnline> selectOnlineByExpired(Date expiredDate)
    {
        String lastAccessTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        return userOnlineDao.selectOnlineByExpired(lastAccessTime);
    }

	public void kickout(String sessionId,String loginName) {
		getSessionBysessionId(sessionId).setAttribute("kickout", true);
		// 读取缓存,找到并从队列中移除
		Cache<String, Deque<Serializable>> cache = redisCacheManager
				.getCache(redisCacheManager.getKeyPrefix() + loginName);
		Deque<Serializable> deques = cache.get(loginName);
		for (Serializable deque : deques) {
			if (sessionId.equals(deque)) {
				deques.remove(deque);
				break;
			}
		}
		cache.put(loginName, deques);
	}
	
	private Session getSessionBysessionId(Serializable sessionId) {
		Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(sessionId));
		return kickoutSession;
	}
}
