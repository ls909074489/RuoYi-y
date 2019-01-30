package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.Blog;

/**
 * 公告 服务层
 * 
 * @author ruoyi
 */
public interface IBlogService
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
     * 保存公告
     * 
     * @param blog 公告信息
     * @return 结果
     */
    public int saveBlog(Blog blog);

    /**
     * 删除公告信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBlogByIds(String ids);

    /**
     * 根据创建人查询
     * @param createBy
     * @return
     */
	public List<Blog> listByCreator(Blog blog);

	/**
	 * 查看次数+1
	 * @param blogId
	 * @return
	 */
	public int addViewCount(Integer blogId);
	
	/**
	 * 回复次数+1
	 * @param blogId
	 * @return
	 */
	public int addReplyCount(Integer blogId,String replyContent);

}
