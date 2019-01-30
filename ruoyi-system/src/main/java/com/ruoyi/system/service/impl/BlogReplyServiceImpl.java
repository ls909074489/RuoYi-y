package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.domain.BlogReply;
import com.ruoyi.system.mapper.BlogReplyMapper;
import com.ruoyi.system.service.IBlogReplyService;

/**
 * 公告 服务层实现
 * 
 * @author ruoyi
 * @date 2018-06-25
 */
@Service
public class BlogReplyServiceImpl implements IBlogReplyService
{
    @Autowired
    private BlogReplyMapper blogReplyMapper;

	@Override
	public List<BlogReply> selectBlogList(Integer blogId) {
		return blogReplyMapper.selectBlogList(blogId);
	}

	@Override
	public int addReply(Integer blogId, String replyContent) {
		BlogReply reply=new BlogReply();
		reply.setBlogId(blogId);
		reply.setReplyContent(replyContent);
		return blogReplyMapper.addReply(reply);
	}


}
