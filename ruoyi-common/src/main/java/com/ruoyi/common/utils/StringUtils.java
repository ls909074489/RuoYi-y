package com.ruoyi.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.impl.util.Base64;

import com.ruoyi.common.support.StrFormatter;

/**
 * 字符串工具类
 * 
 * @author ruoyi
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	private static final Log logger = LogFactory.getLog(StringUtils.class);

	/** 空字符串 */
	private static final String NULLSTR = "";

	/** 下划线 */
	private static final char SEPARATOR = '_';

	/**
	 * 获取参数不为空值
	 * 
	 * @param value
	 *            defaultValue 要判断的value
	 * @return value 返回值
	 */
	public static <T> T nvl(T value, T defaultValue) {
		return value != null ? value : defaultValue;
	}

	/**
	 * * 判断一个Collection是否为空， 包含List，Set，Queue
	 * 
	 * @param coll
	 *            要判断的Collection
	 * @return true：为空 false：非空
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return isNull(coll) || coll.isEmpty();
	}

	/**
	 * * 判断一个Collection是否非空，包含List，Set，Queue
	 * 
	 * @param coll
	 *            要判断的Collection
	 * @return true：非空 false：空
	 */
	public static boolean isNotEmpty(Collection<?> coll) {
		return !isEmpty(coll);
	}

	/**
	 * * 判断一个对象数组是否为空
	 * 
	 * @param objects
	 *            要判断的对象数组
	 ** @return true：为空 false：非空
	 */
	public static boolean isEmpty(Object[] objects) {
		return isNull(objects) || (objects.length == 0);
	}

	/**
	 * * 判断一个对象数组是否非空
	 * 
	 * @param objects
	 *            要判断的对象数组
	 * @return true：非空 false：空
	 */
	public static boolean isNotEmpty(Object[] objects) {
		return !isEmpty(objects);
	}

	/**
	 * * 判断一个Map是否为空
	 * 
	 * @param map
	 *            要判断的Map
	 * @return true：为空 false：非空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return isNull(map) || map.isEmpty();
	}

	/**
	 * * 判断一个Map是否为空
	 * 
	 * @param map
	 *            要判断的Map
	 * @return true：非空 false：空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * * 判断一个字符串是否为空串
	 * 
	 * @param str
	 *            String
	 * @return true：为空 false：非空
	 */
	public static boolean isEmpty(String str) {
		return isNull(str) || NULLSTR.equals(str.trim());
	}

	/**
	 * * 判断一个字符串是否为非空串
	 * 
	 * @param str
	 *            String
	 * @return true：非空串 false：空串
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * * 判断一个对象是否为空
	 * 
	 * @param object
	 *            Object
	 * @return true：为空 false：非空
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

	/**
	 * * 判断一个对象是否非空
	 * 
	 * @param object
	 *            Object
	 * @return true：非空 false：空
	 */
	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}

	/**
	 * * 判断一个对象是否是数组类型（Java基本型别的数组）
	 * 
	 * @param object
	 *            对象
	 * @return true：是数组 false：不是数组
	 */
	public static boolean isArray(Object object) {
		return isNotNull(object) && object.getClass().isArray();
	}

	/**
	 * 去空格
	 */
	public static String trim(String str) {
		return (str == null ? "" : str.trim());
	}

	/**
	 * 截取字符串
	 * 
	 * @param str
	 *            字符串
	 * @param start
	 *            开始
	 * @return 结果
	 */
	public static String substring(final String str, int start) {
		if (str == null) {
			return NULLSTR;
		}

		if (start < 0) {
			start = str.length() + start;
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return NULLSTR;
		}

		return str.substring(start);
	}

	/**
	 * 截取字符串
	 * 
	 * @param str
	 *            字符串
	 * @param start
	 *            开始
	 * @param end
	 *            结束
	 * @return 结果
	 */
	public static String substring(final String str, int start, int end) {
		if (str == null) {
			return NULLSTR;
		}

		if (end < 0) {
			end = str.length() + end;
		}
		if (start < 0) {
			start = str.length() + start;
		}

		if (end > str.length()) {
			end = str.length();
		}

		if (start > end) {
			return NULLSTR;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
	 * 
	 * @param template
	 *            文本模板，被替换的部分用 {} 表示
	 * @param params
	 *            参数值
	 * @return 格式化后的文本
	 */
	public static String format(String template, Object... params) {
		if (isEmpty(params) || isEmpty(template)) {
			return template;
		}
		return StrFormatter.format(template, params);
	}

	/**
	 * 下划线转驼峰命名
	 */
	public static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 是否包含字符串
	 * 
	 * @param str
	 *            验证字符串
	 * @param strs
	 *            字符串组
	 * @return 包含返回true
	 */
	public static boolean inStringIgnoreCase(String str, String... strs) {
		if (str != null && strs != null) {
			for (String s : strs) {
				if (str.equalsIgnoreCase(trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
	 * 例如：HELLO_WORLD->HelloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String convertToCamelCase(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母大写
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String[] camels = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 首字母大写
			result.append(camel.substring(0, 1).toUpperCase());
			result.append(camel.substring(1).toLowerCase());
		}
		return result.toString();
	}

	/**
	 * 首字母小写
	 * 
	 * @param content
	 * @return
	 */
	public static String getFirstLower(String content) {
		return content.substring(0, 1).toLowerCase() + content.substring(1);
	}

	/**
	 * 首字母大写
	 * 
	 * @param content
	 * @return
	 */
	public static String getFirstUpper(String content) {
		return content.substring(0, 1).toUpperCase() + content.substring(1);
	}

	/**
	 * 连接字符串列表
	 * 
	 * @param list
	 *            字符串列表
	 * @param split
	 *            连接字符
	 * @return
	 */
	public static String join(List<String> list, String split) {
		if (list == null || list.size() == 0) {
			return StringUtils.EMPTY;
		}
		String result = "";
		for (String s : list) {
			result += s + split;
		}
		if (list.size() > 0) {
			result = result.substring(0, result.length() - split.length());
		}
		return result;
	}

	/**
	 * 连接字符串列表
	 * 
	 * @param list
	 *            字符串列表
	 * @param split
	 *            连接字符
	 * @return
	 */
	public static String join(String[] list, String split) {
		if (list == null || list.length == 0) {
			return StringUtils.EMPTY;
		}
		String result = "";
		for (String s : list) {
			result += s + split;
		}
		if (list.length > 0) {
			result = result.substring(0, result.length() - split.length());
		}
		return result;
	}

	/**
	 * 使用gzip进行压缩(压缩比例大于zip,只有当字符串很大时才使用压缩)
	 * 
	 * @param text
	 * @return
	 */
	public static String gzip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(text.getBytes());
		} catch (IOException e) {
			logger.error("对字符串进行gzip异常", e);
		} finally {
			IOUtils.closeQuietly(gzip);
		}
		return new String(Base64.encode(out.toByteArray()));
	}

	/**
	 * 使用gzip进行解压缩
	 * 
	 * @param text
	 * @return
	 */
	public static String ungzip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ingzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = Base64.decode(text.getBytes());
			in = new ByteArrayInputStream(compressed);
			ingzip = new GZIPInputStream(in);
			IOUtils.copy(ingzip, out);
			decompressed = out.toString();
		} catch (IOException e) {
			logger.error("对字符串进行ungzip异常", e);
		} finally {
			IOUtils.closeQuietly(ingzip);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return decompressed;
	}

	/**
	 * 使用zip进行压缩(压缩比例小于gzip，只有当字符串很大时才使用压缩)
	 * 
	 * @param text
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(text.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new String(Base64.encode(compressed));
		} catch (IOException e) {
			logger.error("对字符串进行zip异常", e);
		} finally {
			IOUtils.closeQuietly(zout);
			IOUtils.closeQuietly(out);
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Base64.decode(text.getBytes());
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			IOUtils.copy(zin, out);
			decompressed = out.toString();
		} catch (IOException e) {
			logger.error("对字符串进行unzip异常", e);
		} finally {
			IOUtils.closeQuietly(zin);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return decompressed;
	}

	/**
	 * 从文本中获取第一个数字
	 * 
	 * @param text
	 * @return
	 */
	public static String getFirstNumber(String text) {
		String number = "";
		boolean get = false;
		int length = text.length();
		for (int i = 0; i < length; i++) {
			String l = text.substring(i, i + 1);
			if (NumberUtils.isDigits(l)) {
				get = true;
				number += l;
			}
			if (get && !NumberUtils.isDigits(l)) {
				break;
			}
		}
		return number;
	}
}