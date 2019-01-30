package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Blog;
import com.ruoyi.system.mapper.BlogMapper;
import com.ruoyi.system.service.IBlogReplyService;
import com.ruoyi.system.service.IBlogService;

/**
 * 公告 服务层实现
 * 
 * @author ruoyi
 * @date 2018-06-25
 */
@Service
public class BlogServiceImpl implements IBlogService
{
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private IBlogReplyService blogReplyService;
    /**
     * 查询公告信息
     * 
     * @param blogId 公告ID
     * @return 公告信息
     */
    @Override
    public Blog selectBlogById(Integer blogId)
    {
        return blogMapper.selectBlogById(blogId);
    }

    /**
     * 查询公告列表
     * 
     * @param blog 公告信息
     * @return 公告集合
     */
    @Override
    public List<Blog> selectBlogList(Blog blog)
    {
        return blogMapper.selectBlogList(blog);
    }

    /**
     * 新增公告
     * 
     * @param blog 公告信息
     * @return 结果
     */
    @Override
    public int insertBlog(Blog blog)
    {
//    	blog.setCreateBy(ShiroUtils.getLoginName());
        return blogMapper.insertBlog(blog);
    }

    /**
     * 修改公告
     * 
     * @param blog 公告信息
     * @return 结果
     */
    @Override
    public int updateBlog(Blog blog)
    {
        return blogMapper.updateBlog(blog);
    }

    /**
     * 保存公告
     * 
     * @param blog 公告信息
     * @return 结果
     */
    @Override
    public int saveBlog(Blog blog)
    {
        Integer blogId = blog.getBlogId();
        int rows = 0;
        if (StringUtils.isNotNull(blogId))
        {
//            blog.setUpdateBy(ShiroUtils.getLoginName());
            // 修改公告
            rows = blogMapper.updateBlog(blog);
        }
        else
        {
//            blog.setCreateBy(ShiroUtils.getLoginName());
            // 新增公告
            rows = blogMapper.insertBlog(blog);
        }
        return rows;
    }

    /**
     * 删除公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBlogByIds(String ids)
    {
        return blogMapper.deleteBlogByIds(Convert.toStrArray(ids));
    }

    
    /**
     * 根据创建人查询
     * @param createBy
     * @return
     */
	@Override
	public List<Blog> listByCreator(Blog blog) {
		return blogMapper.listByCreator(blog);
	}

	@Override
	public int addViewCount(Integer blogId) {
		return blogMapper.addViewCount(blogId);
	}

	@Override
	public int addReplyCount(Integer blogId,String replyContent) {
		blogReplyService.addReply(blogId,replyContent);
		return blogMapper.addReplyCount(blogId);
	}

}
