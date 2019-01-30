package com.ruoyi.system.domain;

import com.ruoyi.common.base.BaseEntity;

/**
 * 公告表 sys_blog
 * 
 * @author ruoyi
 */
public class Blog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Integer blogId;
    /** 公告标题 */
    private String blogTitle;
    /** 公告类型（1通知 2公告） */
    private String blogType;
    /** 公告内容 */
    private String blogContent;
    /** 公告状态（0正常 1关闭） */
    private String status;
    
    private Integer viewCount=0;//查看次数
    
    private Integer replyCount=0;//回复次数

    public void setBlogId(Integer blogId)
    {
        this.blogId = blogId;
    }

    public Integer getBlogId()
    {
        return blogId;
    }

    public void setBlogTitle(String blogTitle)
    {
        this.blogTitle = blogTitle;
    }

    public String getBlogTitle()
    {
        return blogTitle;
    }

    public void setBlogType(String blogType)
    {
        this.blogType = blogType;
    }

    public String getBlogType()
    {
        return blogType;
    }

    public void setBlogContent(String blogContent)
    {
        this.blogContent = blogContent;
    }

    public String getBlogContent()
    {
        return blogContent;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
}
