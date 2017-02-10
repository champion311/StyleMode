package shinerich.com.stylemodel.common;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量值
 *
 * @author hunk
 */
public abstract class GloableValues {
    //首次安装
    public final static String IS_FIRST_INSTALL = "is_first_install";

    public final static String FIRST_UPDATE_DATE = "first_update_date";
    //是否更新
    public static boolean is_first_update = true;

    //账号平台来源
    public final static String PLATFORM_TAG = "platform_tag";
    public final static int WB_TAG = 0;                      //新浪
    public final static int WX_TAG = 1;                      //微信
    public final static int QQ_TAG = 2;                      //qq


    /**
     * 程序保存根路径
     */
    @SuppressLint("SdCardPath")
    public static String BASE_PATH = "/data/data/shinerich.com.stylemode/files";
    /**
     * 发现-历史数据
     */
    public static List<String> historyDatas = new ArrayList<>();


}
