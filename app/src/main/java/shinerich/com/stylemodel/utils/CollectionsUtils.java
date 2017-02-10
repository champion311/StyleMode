package shinerich.com.stylemodel.utils;

import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 * 
 * @author hunk
 * 
 */
public class CollectionsUtils {
	/**
	 * 判断是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {

		if (map == null || map.size() == 0) {
			return true;
		}
		return false;

	}

	/**
	 * 判断是否为空
	 */
	public static boolean isEmpty(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;

	}
}
