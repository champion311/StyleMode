package shinerich.com.stylemodel;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;


import com.bilibili.magicasakura.utils.ThemeUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

import java.io.File;

import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.inject.component.AppComponent;
import shinerich.com.stylemodel.inject.component.DaggerAppComponent;
import shinerich.com.stylemodel.inject.module.AppModule;
import shinerich.com.stylemodel.utils.FileUtils;
import shinerich.com.stylemodel.utils.MLog;
import shinerich.com.stylemodel.utils.SDCardUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;

/**
 * Created by Administrator on 2016/8/26.
 */
public class AppApplication extends Application implements ThemeUtils.switchColor {

    private static AppApplication application;
    private static AppComponent appComponent;
    private boolean isDug = true;

    static {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //默认取消夜间模式
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化注入
        initInject();
        //初始化目录
        initParams();
        //初始化友盟
        initUM();

        ThemeUtils.setSwitchColor(this);

    }

    public boolean isDug() {
        return isDug;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static AppApplication getAppContext() {
        return application;
    }

    /**
     * 初始化参数
     */
    private void initParams() {


        //设置日志模式
        isDug = BuildConfig.DEBUG;
        MLog.setDug(isDug);
        //初始化目录
        GloableValues.BASE_PATH = SDCardUtils.getPath() + File.separator + "stylemode";
        FileUtils.createDir(GloableValues.BASE_PATH);

    }

    /**
     * 初始化友盟第三方
     */
    private void initUM() {
        //初始化API
        UMShareAPI.get(this);
        //取消dialog
        Config.dialogSwitch = false;
        //取消日志
        Log.LOG = isDug;
        //新浪回调
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        // 微信配置
        //测试
//        String wxAppID = "wx9eaf3a0d79d4d4be";
//        String wxAppSecret = "209e31f90c4b8b4c49082566f6f2e04d";
        String wxAppID = "wx7630d07013c180a3";
        String wxAppSecret = "fb9ac37a197a60508d5eeb854177186e";
        PlatformConfig.setWeixin(wxAppID, wxAppSecret);

        // QQ分享配置
        String qqAppId = "1104871764";
        String qqAppKEY = "yLhSulNAhpLrQSMQ";
        PlatformConfig.setQQZone(qqAppId, qqAppKEY);

        // 新浪微博配置
        String xlAppId = "1646950930";
        String xlAppKEY = "861ccadb565a979a716b4e326ebb89b6";
        PlatformConfig.setSinaWeibo(xlAppId, xlAppKEY);
    }


    public void initInject() {
        appComponent =
                DaggerAppComponent.builder().appModule(new AppModule(this)).build();


    }

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return context.getResources().getColor(colorId);
        }
        String theme = getTheme(context);
        if (theme != null) {
            colorId = getThemeColorId(context, colorId, theme);
        }
        return context.getResources().getColor(colorId);
    }


    @Deprecated
    @Override
    public int replaceColor(Context context, @ColorInt int originColor) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return originColor;
        }
        String theme = getTheme(context);
        int colorId = -1;

        if (theme != null) {
            colorId = getThemeColor(context, originColor, theme);
        }
        return colorId != -1 ? getResources().getColor(colorId) : originColor;
    }

    private String getTheme(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return "red";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_DEFAULT) {
            return "theme_color_primary";//默认主题颜色
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_NIGHT) {
            return "night";
        }
        return null;
    }

    private
    @ColorRes
    int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case R.color.theme_color_primary:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case R.color.theme_color_primary_dark:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case R.color.theme_color_primary_trans:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
            case R.color.theme_color_primary_common_background:
                return context.getResources().getIdentifier(theme + "_common_background", "color", getPackageName());
            case R.color.theme_color_primary_card_view_bg:
                return context.getResources().getIdentifier(theme + "_card_view_bg", "color", getPackageName());
            case R.color.theme_color_primary_common_text_color:
                return context.getResources().getIdentifier(theme + "_common_text_color", "color", getPackageName());

        }
        return colorId;
    }


    @Deprecated
    private
    @ColorRes
    int getThemeColor(Context context, int color, String theme) {
        switch (color) {
            case 0xff23282d:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case 0xff32373f:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case 0xaa000000:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
            case 0xffededed:
                return context.getResources().
                        getIdentifier(theme + "_common_background", "color", getPackageName());
            case 0xff303030:
                return context.getResources().
                        getIdentifier(theme + "_common_text_color", "color", getPackageName());

        }
        return -1;
    }


}
