package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.Blog;

/**
 * 公告 数据层
 * 
 * @author ruoyi
 */
public interface BlogMapper
{
    /**
     * 查询公告信息
     * 
     * @param blogId 公告ID
     * @return 公告信息
     */
    public Blog selectBlogById(Integer blogId);

    /**
     * 查询公告列表
     * 
     * @param blog 公告信息
     * @return 公告集合
     */
    public List<Blog> selectBlogList(Blog blog);

    /**
     * 新增公告
     * 
     * @param blog 公告信息
     * @return 结果
     */
    public int insertBlog(Blog blog);

    /**
     * 修改公告
     * 
     * @param blog 公告信息
     * @return 结果
     */
    public int updateBlog(Blog blog);

    /**
     * 批量删除公告
     * 
     * @param blogIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBlogByIds(String[] blogIds);

    /**
     * 根据创建人查询
     * @param createBy
     * @return
     */
	public List<Blog> listByCreator(Blog blog);

	
	public int addViewCount(Integer blogId);

	public int addReplyCount(Integer blogId);

}