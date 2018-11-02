package com.ruoyi.web.controller.monitor;

import java.io.Serializable;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.domain.UserSessionVo;
import com.ruoyi.system.service.impl.SysUserOnlineServiceImpl;
import com.ruoyi.web.core.base.BaseController;

/**
 * 在线用户监控
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController
{
    private String prefix = "monitor/online";

    @Autowired
    private SysUserOnlineServiceImpl userOnlineService;


    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online()
    {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserOnline userOnline)
    {
        startPage();
        List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:online:batchForceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(@RequestBody List<UserSessionVo> sessions)
    {
//    	@RequestParam("ids[]") String[] ids,
//        for (String sessionId : ids)
//        {
//            SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
//            if (online == null)
//            {
//                return error("用户已下线");
//            }
//            OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
//            if (onlineSession == null)
//            {
//                return error("用户已下线");
//            }
//            if (sessionId.equals(ShiroUtils.getSessionId()))
//            {
//                return error("当前登陆用户无法强退");
//            }
//            onlineSession.setStatus(OnlineStatus.off_line);
//            online.setStatus(OnlineStatus.off_line);
//            userOnlineService.saveOnline(online);
//        }
//        return success();
    	
    	 try {
             //要踢出的用户中是否有自己
             boolean hasOwn=false;
             Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
             for (UserSessionVo sessionVo : sessions) {
                 if(sessionVo.getSessionId().equals(sessionId)){
                     hasOwn=true;
                 }else{
                	 userOnlineService.kickout(sessionVo.getSessionId(),sessionVo.getUsername());
                 }
             }
             if(hasOwn){
                 return success("不能踢出自己");
             }
             return success("踢出用户成功");
         } catch (Exception e) {
             return error("踢出用户失败");
         }
    }

    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/forceLogout")
    @ResponseBody
    public AjaxResult forceLogout(String sessionId,String loginName)
    {
//        SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
//        if (sessionId.equals(ShiroUtils.getSessionId()))
//        {
//            return error("当前登陆用户无法强退");
//        }
//        if (online == null)
//        {
//            return error("用户已下线");
//        }
//        OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
//        if (onlineSession == null)
//        {
//            return error("用户已下线");
//        }
//        onlineSession.setStatus(OnlineStatus.off_line);
//        online.setStatus(OnlineStatus.off_line);
//        userOnlineService.saveOnline(online);
    	
    	 try {
             if(SecurityUtils.getSubject().getSession().getId().equals(sessionId)){
                 return error("不能踢出自己");
             }
             userOnlineService.kickout(sessionId,loginName);
             return success("踢出用户成功");
         } catch (Exception e) {
        	 e.getMessage();
             return error("踢出用户失败");
         }
    }
}
