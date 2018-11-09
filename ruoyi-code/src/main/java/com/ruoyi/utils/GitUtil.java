package com.ruoyi.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * git操作工具类
 * 
 * @author caozhejun
 *
 */
public class GitUtil {

	private static final Log logger = LogFactory.getLog(GitUtil.class);

	/**
	 * 拷贝远程git到本地仓库
	 * 
	 * @param remoteUri
	 *            远程git地址
	 * @param localPath
	 *            本地文件目录
	 * @param branch
	 *            分支
	 * @return
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git clone(String remoteUri, String localPath, String branch) throws InvalidRemoteException, TransportException, GitAPIException {
		logger.info("begin to clone:" + remoteUri);
		Git git = Git.cloneRepository().setURI(remoteUri).setBranch(branch).setDirectory(new File(localPath)).call();
		logger.info("end to clone:" + remoteUri);
		return git;
	}

	/**
	 * 
	 * 拷贝远程git到本地仓库
	 * 
	 * @param remoteUri
	 *            远程git地址
	 * @param localPath
	 *            本地文件目录
	 * @param branch
	 *            分支
	 * @param name
	 *            账号
	 * @param password
	 *            密码
	 * @return
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git clone(String remoteUri, String localPath, String branch, String name, String password) throws InvalidRemoteException, TransportException, GitAPIException {
		logger.info("begin to clone:" + remoteUri);
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		Git git = Git.cloneRepository().setURI(remoteUri).setBranch(branch).setCredentialsProvider(cp).setDirectory(new File(localPath)).call();
		logger.info("end to clone:" + remoteUri);
		return git;
	}

	/**
	 * 拷贝远程git到本地仓库(master)
	 * 
	 * @param remoteUri
	 *            远程git地址
	 * @param localPath
	 *            本地文件目录
	 * @return
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git clone(String remoteUri, String localPath) throws InvalidRemoteException, TransportException, GitAPIException {
		logger.info("begin to clone:" + remoteUri);
		Git git = Git.cloneRepository().setURI(remoteUri).setDirectory(new File(localPath)).call();
		logger.info("end to clone:" + remoteUri);
		return git;
	}

	/**
	 * 拷贝远程git到本地仓库(master)
	 * 
	 * @param remoteUri
	 *            远程git地址
	 * @param localPath
	 *            本地文件目录
	 * @param name
	 *            账号
	 * @param password
	 *            密码
	 * @return
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git clone(String remoteUri, String localPath, String name, String password) throws InvalidRemoteException, TransportException, GitAPIException {
		logger.info("begin to clone:" + remoteUri);
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		Git git = Git.cloneRepository().setURI(remoteUri).setCredentialsProvider(cp).setDirectory(new File(localPath)).call();
		logger.info("end to clone:" + remoteUri);
		return git;
	}

	/**
	 * 创建本地仓库
	 * 
	 * @param localPath
	 *            本地文件目录
	 */
	public static void createLocalRepository(String localPath) {
		Repository repository = null;
		try {
			localPath += "/.git";
			repository = new FileRepository(localPath);
			repository.create();
			logger.info("创建本地仓库成功:" + localPath);
		} catch (Exception e) {
			logger.error("创建本地仓库发生异常:" + localPath, e);
		} finally {
			if (repository != null) {
				repository.close();
			}
		}
	}

	/**
	 * 获取git对象
	 * 
	 * @param localPath
	 *            本地文件目录
	 * @return
	 * @throws IOException
	 */
	public static Git get(String localPath) throws IOException {
		localPath += "/.git";
		Git git = Git.open(new File(localPath));
		return git;
	}

	/**
	 * pull请求
	 * 
	 * @param localPath
	 *            本地文件目录
	 * @param branch
	 *            分支
	 * @param name
	 *            账号
	 * @param password
	 *            密码
	 * @return
	 * @throws IOException
	 * @throws WrongRepositoryStateException
	 * @throws InvalidConfigurationException
	 * @throws InvalidRemoteException
	 * @throws CanceledException
	 * @throws RefNotFoundException
	 * @throws RefNotAdvertisedException
	 * @throws NoHeadException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git pull(String localPath, String branch, String name, String password) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException,
			CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException {
		Git git = get(localPath);
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		git.pull().setRemoteBranchName(branch).setCredentialsProvider(cp).call();
		logger.info("Pull本地仓库成功:" + localPath);
		return git;
	}

	/**
	 * pull请求
	 * 
	 * @param localPath
	 *            本地文件目录
	 * @param branch
	 *            分支
	 * @return
	 * @throws IOException
	 * @throws WrongRepositoryStateException
	 * @throws InvalidConfigurationException
	 * @throws InvalidRemoteException
	 * @throws CanceledException
	 * @throws RefNotFoundException
	 * @throws RefNotAdvertisedException
	 * @throws NoHeadException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git pull(String localPath, String branch) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException,
			RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException {
		Git git = get(localPath);
		git.pull().setRemoteBranchName(branch).call();
		logger.info("Pull本地仓库成功:" + localPath);
		return git;
	}

	/**
	 * pull请求
	 * 
	 * @param localPath
	 *            本地文件目录
	 * @param name
	 *            账号
	 * @param password
	 *            密码
	 * @return
	 * @throws IOException
	 * @throws WrongRepositoryStateException
	 * @throws InvalidConfigurationException
	 * @throws InvalidRemoteException
	 * @throws CanceledException
	 * @throws RefNotFoundException
	 * @throws RefNotAdvertisedException
	 * @throws NoHeadException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git pull(String localPath, String name, String password) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException,
			RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException {
		Git git = get(localPath);
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		git.pull().setCredentialsProvider(cp).call();
		logger.info("Pull本地仓库成功:" + localPath);
		return git;
	}

	/**
	 * pull请求
	 * 
	 * @param localPath
	 *            本地文件目录
	 * @return
	 * @throws IOException
	 * @throws WrongRepositoryStateException
	 * @throws InvalidConfigurationException
	 * @throws InvalidRemoteException
	 * @throws CanceledException
	 * @throws RefNotFoundException
	 * @throws RefNotAdvertisedException
	 * @throws NoHeadException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	public static Git pull(String localPath) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException, RefNotFoundException,
			RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException {
		Git git = get(localPath);
		git.pull().call();
		logger.info("Pull本地仓库成功:" + localPath);
		return git;
	}

	/**
	 * 关闭git
	 * 
	 * @param git
	 */
	public static void close(Git git) {
		if (git != null) {
			git.close();
		}
	}

	/**
	 * 提交修改到远程git仓库
	 * 
	 * @param localPath
	 *            本地文件目录
	 * @param message
	 *            提交注释
	 * @param name
	 *            账号
	 * @param password
	 *            密码
	 * @throws IOException
	 * @throws NoHeadException
	 * @throws NoMessageException
	 * @throws UnmergedPathsException
	 * @throws ConcurrentRefUpdateException
	 * @throws WrongRepositoryStateException
	 * @throws AbortedByHookException
	 * @throws GitAPIException
	 */
	public static void commitAndPush(String localPath, String message, String name, String password)
			throws IOException, NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, AbortedByHookException, GitAPIException {
		Git git = get(localPath);
		git.add().addFilepattern(".").call();
		git.commit().setAll(true).setMessage(message).call();
		logger.info("Commit本地仓库成功:" + localPath);
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(name, password);
		git.push().setForce(true).setCredentialsProvider(cp).setPushAll().call();
		logger.info("Push本地仓库成功:" + localPath);
	}

}
