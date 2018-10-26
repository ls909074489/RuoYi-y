package com.ruoyi.web.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.CookieUtils;
import com.ruoyi.system.domain.SysMenu;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.core.base.BaseController;

/**
 * 首页 业务处理
 * 
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap,HttpServletRequest request)
    {
        // 取身份信息
        SysUser user = getUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        
		// 加载模版
		String theme = getTheme(request);
        return "index_" + theme;
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", Global.getVersion());
        return "main";
    }
    
    
    /**
	 * 
	 * @title: getTheme
	 * @description: 加载风格
	 * @param request
	 * @return
	 * @return: String
	 */
	private String getTheme(HttpServletRequest request) {
		// 默认风格
		String theme = Global.getConfig("admin.default.theme");
		if (StringUtils.isEmpty(theme)) {
			theme = "uadmin";
		}
		// cookies配置中的模版
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
				continue;
			}
			if (cookie.getName().equalsIgnoreCase("theme")) {
				theme = cookie.getValue();
			}
		}
		return theme;
	}

	/**
	 * Coookie设置
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request,
			HttpServletResponse response) {
		if (!StringUtils.isEmpty(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		} else {
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:" + request.getParameter("url");
	}

}
