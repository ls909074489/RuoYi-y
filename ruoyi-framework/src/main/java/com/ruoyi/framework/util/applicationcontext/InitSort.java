package com.ruoyi.framework.util.applicationcontext;
import java.util.Comparator;

/**
 * 执行spring容器启动完成之后的任务排序
 * 
 * @author caozhejun
 *
 */
public class InitSort implements Comparator<ApplicationContextInit> {

	@Override
	public int compare(ApplicationContextInit o1, ApplicationContextInit o2) {
		int sort1 = o1.sort();
		int sort2 = o2.sort();
		int compare = 1;
		if (sort1 == sort2) {
			compare = 0;
		} else if (sort1 < sort2) {
			compare = -1;
		}
		return compare;
	}

}
