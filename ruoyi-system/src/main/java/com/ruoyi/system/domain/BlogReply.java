package com.ruoyi.system.domain;

import com.ruoyi.common.base.BaseEntity;

/**
 * 公告表 sys_blog
 * 
 * @author ruoyi
 */
public class BlogReply extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Integer replyId;
    /** 公告ID */
    private Integer blogId;
    /** 回答内容 */
    private String replyContent;
    
    private String status;
    
	public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	public Integer getBlogId() {
		return blogId;
	}
	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
