package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.BlogReply;

/**
 * 公告 数据层
 * 
 * @author ruoyi
 */
public interface BlogReplyMapper{

    /**
     * 查询公告回答列表
     * 
     * @param blog 公告信息
     * @return 公告集合
     */
    public List<BlogReply> selectBlogList(Integer blogId);

    
	public int addReply(BlogReply reply);


}