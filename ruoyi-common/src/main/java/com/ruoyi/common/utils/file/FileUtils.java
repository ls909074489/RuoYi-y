package com.ruoyi.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * 文件处理工具类
 * 
 * @author ruoyi
 */
public class FileUtils {
	
	private static final Log logger = LogFactory.getLog(FileUtils.class);

	/**
	 * 输出指定文件的byte数组
	 * 
	 * @param filename
	 *            文件
	 * @return
	 */
	public static void writeBytes(String filePath, OutputStream os) throws IOException {
		FileInputStream fis = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException(filePath);
			}
			fis = new FileInputStream(file);
			byte[] b = new byte[1024];
			int length;
			while ((length = fis.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 创建目录
	 * 
	 * @param filePath
	 *            - 源文件(可以是文件,也可以是目录)
	 */
	public static void mkdirs(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return;
		}
		if (filePath.indexOf(".") != -1) {// 可能是文件
			filePath = filePath.substring(0, filePath.lastIndexOf("/"));
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * copy文件 1. 请确认目标文件所在目录是否存在 2. 请确认当前用户是否有写权限
	 * 
	 * @param inPath
	 *            - 源文件(绝对路径)
	 * @param outPath
	 *            - 目标文件
	 */
	public static void copy(String inPath, String outPath) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(new File(inPath));
			os = new FileOutputStream(new File(outPath));
			IOUtils.copy(is, os);
		} catch (FileNotFoundException e) {
			logger.error("拷贝文件发生异常", e);
		} catch (IOException e) {
			logger.error("拷贝文件发生异常", e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * copy文件 1. 请确认目标文件所在目录是否存在 2. 请确认当前用户是否有写权限
	 * 
	 * @param inFile
	 *            - 源文件
	 * @param outPath
	 *            - 目标文件
	 */
	public static void copy(File inFile, String outPath) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(inFile);
			os = new FileOutputStream(new File(outPath));
			IOUtils.copy(is, os);
		} catch (FileNotFoundException e) {
			logger.error("拷贝文件发生异常", e);
		} catch (IOException e) {
			logger.error("拷贝文件发生异常", e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * copy文件 1. 请确认目标文件所在目录是否存在 2. 请确认当前用户是否有写权限
	 * 
	 * @param is
	 *            - 源文件流
	 * @param outPath
	 *            - 目标文件
	 */
	public static void copy(InputStream is, String outPath) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(outPath));
			IOUtils.copy(is, os);
		} catch (FileNotFoundException e) {
			logger.error("拷贝文件发生异常", e);
		} catch (IOException e) {
			logger.error("拷贝文件发生异常", e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int index = fileName.lastIndexOf(".");
		String ext = "";
		if (index > -1) {
			ext = fileName.substring(index + 1);
		}
		return ext;
	}

	/**
	 * 获取文件对应的windows系统盘符
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getWindowsPanFu(String filePath) {
		String panfu = "";
		int index = filePath.indexOf(":");
		if (index > -1) {
			panfu = filePath.substring(0, index);
		}
		return panfu;
	}

	public static void main(String[] args) {
		System.out.println(getWindowsPanFu("D:\\"));
	}
}
