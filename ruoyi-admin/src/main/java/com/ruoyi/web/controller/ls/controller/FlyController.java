package com.ruoyi.web.controller.ls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.framework.util.ServletUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.core.base.BaseController;

@Controller
@RequestMapping("/pub/fly")
public class FlyController extends BaseController{
	
	private String prefix = "fly";
	
	@Autowired
	private ISysUserService userService;
	
    @RequestMapping("/login")
    public String detailList(Model model){
    	ServletUtils.getRequest().setAttribute(ShiroConstants.CURRENT_CAPTCHA,"123456");
    	ShiroUtils.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY,"123456");
    	System.out.println(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA));
        return prefix + "/login";
    }
    
    @GetMapping("/reg")
    public String reg(Model model){
        return prefix + "/reg";
    }
    
    @ResponseBody
    @PostMapping("/reg")
    public AjaxResult saveReg(SysUser user){
    	user.setLoginName(user.getUserName());
    	user.setPostIds(new Long[0]);
    	user.setRoleIds(new Long[0]);
    	userService.insertUser(user);
    	return AjaxResult.success("");
    }
}
