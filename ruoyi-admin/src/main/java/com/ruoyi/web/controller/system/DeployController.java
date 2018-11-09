package com.ruoyi.web.controller.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ruoyi.common.exception.BussException;
import com.ruoyi.common.json.JsonResult;
import com.ruoyi.common.utils.EncryptUtil;
import com.ruoyi.common.utils.ServerUtil;
import com.ruoyi.deploy.CodeDeploy;
import com.ruoyi.model.DeployForm;
import com.ruoyi.utils.ExecuteUtil;
import com.ruoyi.utils.SystemUtil;

/**
 * 用于接收发布系统文件的Controller
 * 
 * @author caozhejun
 *
 */
@RestController
@RequestMapping("/deploy")
public class DeployController {

	private static final Log logger = LogFactory.getLog(DeployController.class);

	@RequestMapping("/rollback")
	public JsonResult rollback(HttpServletRequest request, String[] fileNames, DeployForm deployForm) throws IOException {
		checkIp(request);
		checkSign(deployForm);
		rollbackFiles(fileNames);
		return new JsonResult();
	}

	/**
	 * 回滚文件对应的服务
	 * 
	 * @param fileNames
	 * @throws IOException
	 */
	private void rollbackFiles(String[] fileNames) throws IOException {
		for (String fileName : fileNames) {
			rollbackFile(fileName);
		}
	}

	/**
	 * 回滚文件对应的服务
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	private void rollbackFile(String fileName) throws IOException {
		if (SystemUtil.isWindowsOs()) {
			rollbackFileWindows(fileName);
		} else {
			rollbackFileLinux(fileName);
		}
	}

	private void rollbackFileWindows(String fileName) throws IOException {
		String serverDir = CodeDeploy.autoDeployServerDir;
		File backFile = new File(serverDir + "/" + fileName + ".bak");
		if (!backFile.exists()) {
			logger.error(backFile.getAbsolutePath() + "不存在，无法回滚");
			return;
		}
		File jarFile = new File(serverDir + "/" + fileName);
		FileUtils.copyFile(backFile, jarFile);
		// 关闭运行的进程
		killJarProcessWindows(fileName);
		// 启动服务
		int lastIndex = fileName.lastIndexOf(".");
		String name = fileName.substring(0, lastIndex);
		String scriptFile = serverDir + "/" + name + ".bat";
		ExecuteUtil.executeBat(scriptFile);
	}

	private void rollbackFileLinux(String fileName) throws IOException {
		String serverDir = CodeDeploy.autoDeployServerDir;
		File backFile = new File(serverDir + "/" + fileName + ".bak");
		if (!backFile.exists()) {
			logger.error(backFile.getAbsolutePath() + "不存在，无法回滚");
			return;
		}
		File jarFile = new File(serverDir + "/" + fileName);
		FileUtils.copyFile(backFile, jarFile);
		// 关注运行的进程
		killJarProcessLinux(fileName);
		// 启动服务
		int lastIndex = fileName.lastIndexOf(".");
		String name = fileName.substring(0, lastIndex);
		String scriptFile = serverDir + "/" + name + ".sh";
		logger.info("启动服务:" + scriptFile);
		ExecuteUtil.execute(scriptFile);
	}

	@RequestMapping("/receive")
	public JsonResult receive(MultipartHttpServletRequest request, MultipartFile file, DeployForm deployForm) throws IllegalStateException, IOException {
		checkIp(request);
		checkSign(deployForm);
		dealFile(file, deployForm);
		return new JsonResult();
	}

	private void dealFile(MultipartFile file, DeployForm deployForm) throws IllegalStateException, IOException {
		if (SystemUtil.isWindowsOs()) {
			dealFileWindows(file, deployForm);
		} else {
			dealFileLinux(file, deployForm);
		}
	}

	private void dealFileWindows(MultipartFile file, DeployForm deployForm) throws IllegalStateException, IOException {
		String serverDir = CodeDeploy.autoDeployServerDir;
		new File(serverDir).mkdirs();
		String fileName = file.getOriginalFilename();
		int lastIndex = fileName.lastIndexOf(".");
		String name = fileName.substring(0, lastIndex);
		String jarFile = serverDir + "/" + fileName;
		String scriptFile = serverDir + "/" + name + ".bat";
		File jar = bakJarFile(jarFile);
		file.transferTo(jar);
		logger.info("文件写入成功" + jarFile);
		String scriptContent = "java " + deployForm.getStartParams() + " -jar " + jarFile;
		FileUtils.writeStringToFile(new File(scriptFile), scriptContent);
		logger.info("脚本写入成功" + scriptFile);
		// 关闭运行的进程
		killJarProcessWindows(fileName);
		// 启动服务
		ExecuteUtil.executeBat(scriptFile);
	}

	/**
	 * 删掉windows里的进程
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	private void killJarProcessWindows(String fileName) throws IOException {
		Process proc = Runtime.getRuntime().exec("wmic process list");
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String info = br.readLine();
		while (info != null) {
			info = br.readLine();
			if (StringUtils.isNotBlank(info) && info.contains(fileName)) {
				logger.info("找到需要删除的进程:" + info);
				String pid = com.ruoyi.common.utils.StringUtils.getFirstNumber(info);
				logger.info("找到需要删除的进程pid:" + pid);
				SystemUtil.killByPid(NumberUtils.toInt(pid));
				logger.info("杀掉进程:" + pid);
			}
		}
	}

	private void dealFileLinux(MultipartFile file, DeployForm deployForm) throws IllegalStateException, IOException {
		String serverDir = CodeDeploy.autoDeployServerDir;
		new File(serverDir).mkdirs();
		String fileName = file.getOriginalFilename();
		// 覆盖服务文件及脚本文件
		String scriptFile = dealFileLinux(file, deployForm, serverDir, fileName);
		// 关注运行的进程
		killJarProcessLinux(fileName);
		// 启动服务
		logger.info("启动服务:" + scriptFile);
		ExecuteUtil.execute(scriptFile);
	}

	private String dealFileLinux(MultipartFile file, DeployForm deployForm, String serverDir, String fileName) throws IOException {
		int lastIndex = fileName.lastIndexOf(".");
		String name = fileName.substring(0, lastIndex);
		String jarFile = serverDir + "/" + fileName;
		String scriptFile = serverDir + "/" + name + ".sh";
		File jar = bakJarFile(jarFile);
		file.transferTo(jar);
		logger.info("文件写入成功" + jarFile);
		String scriptContent = "nohup java " + deployForm.getStartParams() + " -jar " + jarFile + " &";
		FileUtils.writeStringToFile(new File(scriptFile), scriptContent);
		logger.info("脚本写入成功" + scriptFile);
		ExecuteUtil.execute("chmod 777 " + scriptFile);
		logger.info("脚本设置权限成功" + scriptFile);
		return scriptFile;
	}

	/**
	 * 备份jar文件
	 * 
	 * @param jarFile
	 * @return
	 * @throws IOException
	 */
	private File bakJarFile(String jarFile) throws IOException {
		File jar = new File(jarFile);
		if (jar.exists()) {
			File bak = new File(jarFile + ".bak");
			FileUtils.copyFile(jar, bak);
			logger.info("备份文件成功:" + bak.getAbsolutePath());
		}
		return jar;
	}

	private void killJarProcessLinux(String fileName) throws IOException {
		String process = ExecuteUtil.execute("ps -ef");
		logger.info("进程:" + process);
		List<String> lines = IOUtils.readLines(IOUtils.toInputStream(process));
		for (String line : lines) {
			if (line.endsWith(fileName)) {
				logger.info("找到需要kill调的进程:" + line);
				killProcessLinux(line);
			}
		}
	}

	/**
	 * 杀掉进程
	 * 
	 * @param line
	 * @throws IOException
	 */
	private void killProcessLinux(String line) throws IOException {
		String pid = getPidLinux(line);
		// 杀掉进程
		ExecuteUtil.execute("kill -9 " + pid);
		logger.info("杀掉进程:" + pid);
	}

	/**
	 * 获取进程的pid
	 * 
	 * @param line
	 * @return
	 */
	private String getPidLinux(String line) {
		return com.ruoyi.common.utils.StringUtils.getFirstNumber(line);
	}

	private void checkIp(HttpServletRequest request) {
		String ip = ServerUtil.getProxyIp(request);
		logger.info("************************接收到" + ip + "发来的发布应用请求********************************");
		String ips = CodeDeploy.autoDeployIps;
		if (StringUtils.isEmpty(ips)) {
			return;
		}
		String[] iplist = ips.split(",");
		List<String> allowIpList = new ArrayList<>(iplist.length);
		for (String allowIp : iplist) {
			if (StringUtils.isNotEmpty(allowIp)) {
				allowIpList.add(allowIp.trim());
			}
		}
		if (!allowIpList.contains(ip)) {
			throw new BussException("您的IP地址不在白名单中，不能发布应用");
		}
	}

	private void checkSign(DeployForm deployForm) {
		String key = CodeDeploy.autoDeployKey;
		String sign = EncryptUtil.md5(deployForm.getTime() + key);
		if (!sign.equals(deployForm.getSign())) {
			throw new BussException("签名错误");
		}
	}

}
