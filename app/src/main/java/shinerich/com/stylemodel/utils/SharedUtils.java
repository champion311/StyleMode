package shinerich.com.stylemodel.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Shared帮助类
 * 
 * @author hunk
 * 
 */
public class SharedUtils {

	private static final String SHARED_NAME = "style_setting";

	/**
	 * 是否包含key
	 * 
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);

	}

	/**
	 * 移除key
	 * 
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor eiditor = sp.edit();
		eiditor.remove(key);
		eiditor.commit();
	}

	/**
	 * 保存key
	 * 
	 */
	public static void save(Context context, String key, String values) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor eiditor = sp.edit();
		eiditor.putString(key, values);
		eiditor.commit();
	}
	
	/**
	 * 保存key
	 * 
	 */
	public static void save(Context context,String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor eiditor = sp.edit();
		eiditor.putBoolean(key, value);
		eiditor.commit();
	}
	
	/**
	 * 保存key
	 * 
	 */
	public static void save(Context context,String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor eiditor = sp.edit();
		eiditor.putInt(key, value);
		eiditor.commit();
	}

	/**
	 * 获取值
	 * 
	 */
	public static String getStr(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}
	
	/**
	 * 获取值
	 * 
	 */
	public static String getStr(Context context, String key,String def) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, def);
	}
	
	/**
	 * 获取
	 * 
	 */
	public static boolean getBool(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}
	
	/**
	 * 获取
	 * 
	 */
	public static boolean getBool(Context context, String key,boolean def) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, def);
	}
	
	/**
	 * 获取
	 * 
	 */
	public static int getInt(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, 0);
	}
	
	/**
	 * 获取
	 * 
	 */
	public static int getInt(Context context, String key,int def) {
		SharedPreferences sp = context.getSharedPreferences(SHARED_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, def);
	}

}
