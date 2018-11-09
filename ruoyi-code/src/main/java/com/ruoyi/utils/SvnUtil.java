package com.ruoyi.utils;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * 操作svn的工具类
 * 
 * @author caozhejun
 *
 */
public class SvnUtil {

	private static final Log logger = LogFactory.getLog(SvnUtil.class);

	private SvnUtil() {
		init();
	}

	private void init() {
		SVNRepositoryFactoryImpl.setup();
		DAVRepositoryFactory.setup();
		FSRepositoryFactory.setup();
	}

	private static final class SvnUtilHolder {
		private static SvnUtil instance = new SvnUtil();
	}

	public static SvnUtil getInstance() {
		return SvnUtilHolder.instance;
	}

	/**
	 * 从代码库checkout代码
	 * 
	 * @param url
	 *            svn的版本库地址
	 * @param localDir
	 *            本地目录
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @throws SVNException
	 */
	public void checkout(String url, String localDir, String account, String password) throws SVNException {
		SVNURL repositoryURL = SVNURL.parseURIEncoded(url);
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, account, password);
		// 通过客户端管理类获得updateClient类的实例。
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		// sets externals not to be ignored during the checkout
		updateClient.setIgnoreExternals(false);
		// 执行check out 操作，返回工作副本的版本号。
		long workingVersion = updateClient.doCheckout(repositoryURL, new File(localDir), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);
		logger.info("把版本：" + workingVersion + " check out 到目录：" + localDir + "成功");
	}

	/**
	 * 更新代码
	 * 
	 * @param localDir
	 *            本地目录
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @throws SVNException
	 */
	public void update(String localDir, String account, String password) throws SVNException {
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, account, password);
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();

		/*
		 * sets externals not to be ignored during the update
		 */
		updateClient.setIgnoreExternals(false);
		updateClient.doUpdate(new File[] { new File(localDir) }, SVNRevision.HEAD, SVNDepth.INFINITY, false, false);
		logger.info("update svn代码成功:" + localDir);
	}

	/**
	 * 提交代码
	 * 
	 * @param localDir
	 *            本地目录
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @param message
	 *            提交代码的注释
	 * @throws SVNException
	 */
	public void commit(String localDir, String account, String password, String message) throws SVNException {
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, account, password);
		ourClientManager.getCommitClient().doCommit(new File[] { new File(localDir) }, false, message, null, null, false, false, SVNDepth.fromRecurse(true));
		logger.info("提交svn代码成功:" + localDir + "," + message);
	}

}
