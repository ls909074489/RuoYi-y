package com.ruoyi.deploy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNException;

import com.ruoyi.common.utils.EmailUtil;
import com.ruoyi.common.utils.EncryptUtil;
import com.ruoyi.common.utils.HttpClientUtil;
import com.ruoyi.enums.VersionType;
import com.ruoyi.model.DevProject;
import com.ruoyi.model.ProjectPackageResult;
import com.ruoyi.model.ProjectServer;
import com.ruoyi.utils.ExecuteUtil;
import com.ruoyi.utils.GitUtil;
import com.ruoyi.utils.SvnUtil;
import com.ruoyi.utils.SystemUtil;

@Component
public class CodeDeploy {
	
	private static final Log logger = LogFactory.getLog(CodeDeploy.class);
	
	private static final String deployUrl = "/deploy/receive";

	private static final String rollbackUrl = "/deploy/rollback";
	
	@Value("${auto.deploy.local.reps}")
	private String repsDir="D:\\reps";
	
	public static String autoDeployKey = "simbaAutoDeploy";
	public static String autoDeployIps = "";
	public static String autoDeployServerDir = "D:\\servers";
	
	@Resource
	private TaskExecutor taskExecutor;
	@Autowired
	private EmailUtil emailUtil;
	
//	public static void main(String[] args) {
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
//	}
	
	
	public void checkoutCode(DevProject devProject) throws InvalidRemoteException, TransportException, GitAPIException, SVNException {
		logger.info(devProject.toString() + "开发初始化代码:" + devProject.getVersionUrl());
		if (devProject.getVersionType().equals(VersionType.SVN.getName())) {
			// svn拉取代码
			checkoutSvn(devProject);
		} else {
			// git拉取代码
			cloneGit(devProject);
		}
		logger.info(devProject.toString() + "结束初始化代码:" + devProject.getVersionUrl());
	}
	
	/**
	 * 从版本服务器更新代码
	 * 
	 * @param devProject
	 * @throws GitAPIException
	 * @throws IOException
	 * @throws TransportException
	 * @throws NoHeadException
	 * @throws RefNotAdvertisedException
	 * @throws RefNotFoundException
	 * @throws CanceledException
	 * @throws InvalidRemoteException
	 * @throws InvalidConfigurationException
	 * @throws WrongRepositoryStateException
	 * @throws SVNException
	 */
	public void updateCode(DevProject devProject) throws WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException, RefNotFoundException,
			RefNotAdvertisedException, NoHeadException, TransportException, IOException, GitAPIException, SVNException {
		logger.info(devProject.toString() + "开发更新代码:" + devProject.getVersionUrl());
		if (devProject.getVersionType().equals(VersionType.SVN.getName())) {
			// svn更新代码
			updateSvn(devProject);
		} else {
			// git更新代码
			pullGit(devProject);
		}
		logger.info(devProject.toString() + "结束更新代码:" + devProject.getVersionUrl());
	}
	
	/**
	 * 打包发布到服务器
	 * 
	 * @param devProject
	 * @param servers
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws TransportException
	 * @throws NoHeadException
	 * @throws RefNotAdvertisedException
	 * @throws RefNotFoundException
	 * @throws CanceledException
	 * @throws InvalidRemoteException
	 * @throws InvalidConfigurationException
	 * @throws WrongRepositoryStateException
	 * @throws SVNException
	 */
	public void publishServers(DevProject devProject, List<ProjectServer> servers,List<ProjectPackageResult> res) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException,
			CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException, SVNException {
		projectPackage(devProject);
		publishPackageServers(devProject, servers,res);
		taskExecutor.execute(() -> {
			sendNotifyEmail(devProject);
		});
	}
	
	
	/**
	 * 将打包结果发布到服务器
	 * 
	 * @param devProject
	 * @param servers
	 */
	private void publishPackageServers(DevProject devProject, List<ProjectServer> servers,List<ProjectPackageResult> res) {
		if (res.isEmpty()) {
			throw new RuntimeException("没有需要发布的文件");
		}
		res.forEach((ProjectPackageResult r) -> {
			publishServer(r, devProject, servers);
		});
	}

	/**
	 * 将打包结果发布到服务器
	 * 
	 * @param r
	 * @param devProject
	 * @param servers
	 */
	private void publishServer(ProjectPackageResult r, DevProject devProject, List<ProjectServer> servers) {
		String repsPath = repsDir + "/" + devProject.getCode();
		String targetFile = repsPath + "/" + r.getFilePath();
		String key = autoDeployKey;
		long time = System.currentTimeMillis();
		String sign = EncryptUtil.md5(time + key);
		String startParams = devProject.getStartParams();
		Map<String, String> params = new HashMap<>();
		params.put("sign", sign);
		params.put("startParams", startParams);
		params.put("time", time + "");
		int count = servers.size();
		int num = 0;
		for (ProjectServer server : servers) {
			String url = "http://" + server.getIp() + ":" + server.getPort() + deployUrl;
			logger.info("开始发布文件[" + targetFile + "]到" + url);
			String response = HttpClientUtil.fileUpload(url, targetFile, params);
			logger.info("结束发布文件[" + targetFile + "]到" + url + ",返回结果:" + response);
			num++;
			if (num != count) {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					logger.error("休眠失败", e);
				}
			}
		}
	}

	/**
	 * 项目打包
	 * 
	 * @param devProject
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws TransportException
	 * @throws NoHeadException
	 * @throws RefNotAdvertisedException
	 * @throws RefNotFoundException
	 * @throws CanceledException
	 * @throws InvalidRemoteException
	 * @throws InvalidConfigurationException
	 * @throws WrongRepositoryStateException
	 * @throws SVNException
	 */
	private void projectPackage(DevProject devProject) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException,
			RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException, SVNException {
		String repsPath = repsDir + "/" + devProject.getCode();
		if (!new File(repsPath).exists()) {
			reGetCodeFormVersionServer(devProject);
		}
		updateCode(devProject);
		packageServer(devProject);
	}
	
	
	/**
	 * 项目执行打包命令
	 * 
	 * @param devProject
	 * @throws IOException
	 */
	private void packageServer(DevProject devProject) throws IOException {
		String repsPath = repsDir + "/" + devProject.getCode();
		String packageCommandFile = repsPath + "/" + devProject.getPackageCommandFile();
		logger.info(devProject.toString() + "开始打包");
		File cf = new File(packageCommandFile);
		String cfDir = cf.getParent();
		String command = "";
		boolean isWindows = SystemUtil.isWindowsOs();
		if (isWindows) {
			command += "cmd.exe /c  ";
			command += com.ruoyi.common.utils.file.FileUtils.getWindowsPanFu(cfDir) + ": && ";
		} else {
			command += "/bin/sh -c  ";
		}
		command += " cd " + cfDir + " && ";
		if (isWindows) {
			command += cf.getName();
		} else {
			command += " ./" + cf.getName();
		}
		logger.info("执行打包命令:" + command);
		ExecuteUtil.execute(command);
		logger.info(devProject.toString() + "结束打包");
	}
	
	/**
	 * 从版本服务器重新拉代码
	 * 
	 * @param devProject
	 * @throws GitAPIException
	 * @throws TransportException
	 * @throws InvalidRemoteException
	 * @throws SVNException
	 */
	private void reGetCodeFormVersionServer(DevProject devProject) throws InvalidRemoteException, TransportException, GitAPIException, SVNException {
		String repsPath = repsDir + "/" + devProject.getCode();
		FileUtils.deleteQuietly(new File(repsPath));
		getCodeFormVersionServer(devProject);
	}
	
	/**
	 * 从版本服务器拉代码
	 * 
	 * @param devProject
	 * @throws GitAPIException
	 * @throws TransportException
	 * @throws InvalidRemoteException
	 * @throws SVNException
	 */
	private void getCodeFormVersionServer(DevProject devProject) throws InvalidRemoteException, TransportException, GitAPIException, SVNException {
		String repsPath = repsDir + "/" + devProject.getCode();
		new File(repsPath).mkdirs();
		if (StringUtils.isEmpty(devProject.getVersionUrl())) {
			return;
		}
		// 从版本服务器拉取最新的代码
		checkoutCode(devProject);
	}
	
	/**
	 * 从svn上checkout代码
	 * 
	 * @param devProject
	 * @throws SVNException
	 */
	private void checkoutSvn(DevProject devProject) throws SVNException {
		String repsPath = repsDir + "/" + devProject.getCode();
		SvnUtil.getInstance().checkout(devProject.getVersionUrl(), repsPath, devProject.getAccount(), devProject.getPwd());
	}
	
	/**
	 * 从git上clone代码
	 * 
	 * @param devProject
	 * @throws GitAPIException
	 * @throws TransportException
	 * @throws InvalidRemoteException
	 */
	private void cloneGit(DevProject devProject) throws InvalidRemoteException, TransportException, GitAPIException {
		String repsPath = repsDir + "/" + devProject.getCode();
		GitUtil.clone(devProject.getVersionUrl(), repsPath, devProject.getAccount(), devProject.getPwd());
	}
	
	/**
	 * 从git来pull代码
	 * 
	 * @param devProject
	 * @throws GitAPIException
	 * @throws IOException
	 * @throws TransportException
	 * @throws NoHeadException
	 * @throws RefNotAdvertisedException
	 * @throws RefNotFoundException
	 * @throws CanceledException
	 * @throws InvalidRemoteException
	 * @throws InvalidConfigurationException
	 * @throws WrongRepositoryStateException
	 */
	private void pullGit(DevProject devProject) throws WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException, RefNotFoundException,
			RefNotAdvertisedException, NoHeadException, TransportException, IOException, GitAPIException {
		String repsPath = repsDir + "/" + devProject.getCode();
		GitUtil.pull(repsPath, devProject.getAccount(), devProject.getPwd());
	}

	/**
	 * 从svn上update代码
	 * 
	 * @param devProject
	 * @throws SVNException
	 */
	private void updateSvn(DevProject devProject) throws SVNException {
		String repsPath = repsDir + "/" + devProject.getCode();
		SvnUtil.getInstance().update(repsPath, devProject.getAccount(), devProject.getPwd());
	}
	
	/**
	 * 发送打包成功通知邮件
	 * 
	 * @param devProject
	 */
	private void sendNotifyEmail(DevProject devProject) {
		String emails = devProject.getNotifyEmails();
		if (StringUtils.isEmpty(emails)) {
			return;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String title = devProject.getName() + "发布完成[" + simpleDateFormat.format(new Date()) + "]";
		String[] emailList = emails.split(",");
		for (String email : emailList) {
			if (StringUtils.isEmpty(email)) {
				continue;
			}
			emailUtil.send(email, title, title);
			logger.info("发送邮件》》》》》》》》》》》》》》");
		}
	}
}

