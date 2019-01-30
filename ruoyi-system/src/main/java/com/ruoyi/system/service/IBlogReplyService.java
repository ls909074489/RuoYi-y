package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.BlogReply;

/**
 * 公告 服务层
 * 
 * @author ruoyi
 */
public interface IBlogReplyService{

	 /**
     * 查询公告回答列表
     * 
     * @param blog 公告信息
     * @return 公告集合
     */
    public List<BlogReply> selectBlogList(Integer blogId);

    
	public int addReply(Integer blogId, String replyContent);


}
