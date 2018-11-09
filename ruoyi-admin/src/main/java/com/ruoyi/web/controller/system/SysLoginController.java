package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.EmailUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.deploy.CodeDeploy;
import com.ruoyi.framework.util.ServletUtils;
import com.ruoyi.model.DevProject;
import com.ruoyi.model.ProjectPackageResult;
import com.ruoyi.model.ProjectServer;
import com.ruoyi.web.core.base.BaseController;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController {

	private static final Log logger = LogFactory.getLog(SysLoginController.class);
	
	@Autowired
	private CodeDeploy codeDeploy;
	@Autowired
	private EmailUtil emailUtil;
	@Resource
	private TaskExecutor taskExecutor;
	
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
		}
//		DevProject devProject=new DevProject();
//		devProject.setCode("cloudLockAdmin");
//		devProject.setName("云锁Admin(大众版)");		
//		devProject.setVersionType("svn");
//		devProject.setAccount("caozhejun");
//		devProject.setPwd("Aa123456");
//		devProject.setVersionUrl("https://svnserver1:8443/svn/CloudLock/server/trunk/cloudLock");
//		devProject.setPackageCommandFile("package.bat");
//		devProject.setStartParams("-server -Xms200m -Xmx800m");
//		devProject.setCreateTime(new Date());
//		devProject.setNotifyEmails("liusheng@ut.cn,909074489");
//		try {
//			CodeUtils.checkoutCode(devProject);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		DevProject devProject=new DevProject();
//		devProject.setCode("cloudLockAdmin");
//		devProject.setName("云锁Admin(大众版)");		
//		devProject.setVersionType("svn");
//		devProject.setAccount("caozhejun");
//		devProject.setPwd("Aa123456");
//		devProject.setVersionUrl("https://svnserver1:8443/svn/CloudLock/server/trunk/cloudLock");
//		devProject.setPackageCommandFile("package.bat");
//		devProject.setStartParams("-server -Xms200m -Xmx800m");
//		devProject.setCreateTime(new Date());
//		devProject.setNotifyEmails("liusheng@ut.cn,909074489");
//		
//		//需要发布的服务器
//		List<ProjectServer> servers = new ArrayList<ProjectServer>();
//		ProjectServer server = new ProjectServer();
//		server.setName("服务器1");
//		server.setIp("127.0.0.1");
//		server.setPort(8089);
//		servers.add(server);
//		//打包后文件路径
//		List<ProjectPackageResult> resList = new ArrayList<ProjectPackageResult>();
//		ProjectPackageResult ppr=new ProjectPackageResult();
//		ppr.setFilePath("simba-admin/target/cloudLockAdmin.jar");
//		resList.add(ppr);
//		
//		try {
//			codeDeploy.publishServers(devProject, servers, resList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		emailUtil.send("liusheng@ut.cn", "测试邮件", "aaaaaaaaaaaaa");
		
		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return success();
		} catch (AuthenticationException e) {
			String msg = "用户或密码错误";
			if (StringUtils.isNotEmpty(e.getMessage())) {
				msg = e.getMessage();
			}
			return error(msg);
		}
	}

	@GetMapping("/unauth")
	public String unauth() {
		return "/error/unauth";
	}
}
