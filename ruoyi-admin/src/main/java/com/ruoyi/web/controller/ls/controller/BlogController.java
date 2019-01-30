package com.ruoyi.web.controller.ls.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.Blog;
import com.ruoyi.system.domain.BlogReply;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.IBlogReplyService;
import com.ruoyi.system.service.IBlogService;
import com.ruoyi.web.controller.ls.bean.ActionResultModel;
import com.ruoyi.web.core.base.BaseController;

/**
 * 公告 信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/pub/blog")
public class BlogController extends BaseController{

	private String prefix = "fly/blog";
	
    @Autowired
    private IBlogService blogService;
    @Autowired
    private IBlogReplyService blogReplyService;

    @RequestMapping("/{userName}")
    public String detailList(@PathVariable("userName") String userName, Model model){
    	Blog blog=new Blog();
    	blog.setCreateBy(userName);
    	blog.setStatus("0");
    	List<Blog> list = blogService.listByCreator(blog);
    	model.addAttribute("userName", userName);
    	model.addAttribute("list", list);
        return prefix + "/pub_blog_list";
    }

    /**
     * 查看公告
     * @param blogId
     * @param userName
     * @param model
     * @return
     */
    @RequestMapping("/view")
    public String detailView(@RequestParam Integer blogId,@RequestParam String userName, Model model){
    	Blog blog = blogService.selectBlogById(blogId);
    	blogService.addViewCount(blogId);
    	model.addAttribute("blog", blog);
    	model.addAttribute("userName", userName);
        return prefix + "/pub_blog_detail";
    }
    
    /**
     * 新增
     * @param blog
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public AjaxResult addSave(Blog blog)
    {
    	blog.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(blogService.insertBlog(blog));
    }
    
    
    @ResponseBody
    @PostMapping("/saveBlog")
    public AjaxResult saveBlog(Blog blog)
    {
    	blog.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(blogService.saveBlog(blog));
    }
    
    
    
    /**
     * 添加回复
     * @param blogId
     * @param replyContent
     * @return
     */
    @ResponseBody
    @RequestMapping("/addReply")
    public AjaxResult addReply(@RequestParam Integer blogId, String replyContent){
    	blogService.addReplyCount(blogId,replyContent);
        return AjaxResult.success("");
    }
    
    /**
     * 获取回复
     * @param blogId
     * @param replyContent
     * @return
     */
    @ResponseBody
    @RequestMapping("/dataReply")
    public ActionResultModel<BlogReply> dataReply(@RequestParam Integer blogId){
    	ActionResultModel<BlogReply> arm=new ActionResultModel<BlogReply>();
    	List<BlogReply> list= blogReplyService.selectBlogList(blogId);
    	arm.setSuc(true);
    	arm.setMsg((list==null?0:list.size())+"");
    	arm.setRecords(list);
        return arm;
    }
    
    
    @GetMapping("/flyList")
    public String flyList(Model model){
    	SysUser user=ShiroUtils.getUser();
    	Blog blog=new Blog();
    	blog.setCreateBy(user.getLoginName());
    	List<Blog> list = blogService.listByCreator(blog);
    	model.addAttribute("userName", user.getLoginName());
    	model.addAttribute("blogCount", user.getBlogCount());
    	model.addAttribute("list", list);
        return prefix + "/pub_blog_main";
    }
    
    /**
     * 添加
     * @param model
     * @return
     */
    @GetMapping("/flyAdd")
    public String flyAdd(Model model){
        return prefix + "/pub_blog_add";
    }
    
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @GetMapping("/flyEdit/{blogId}")
    public String flyEdit(@PathVariable("blogId") Integer blogId, Model model){
        Blog blog = blogService.selectBlogById(blogId);
        model.addAttribute("blog", blog);
        return prefix + "/pub_blog_edit";
    }
    
    
    @GetMapping("/flyView")
    public String flyView(@RequestParam Integer blogId,Model model){
    	Blog blog = blogService.selectBlogById(blogId);
    	model.addAttribute("blog", blog);
        return prefix + "/pub_blog_view";
    }
  
    
    

	@RequestMapping(value="/logout",method = RequestMethod.GET)
	public String pubBlogLogout(@RequestParam("userName") String userName,Model model) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/pub/blog/"+userName;
	}
}
