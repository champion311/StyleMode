package shinerich.com.stylemodel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;

import com.bilibili.magicasakura.utils.ThemeUtils;

/**
 * Created by Administrator on 2016/12/13.
 */

public class ThemeHelper {

    public static final String CURRENT_THEME = "theme_current";

    public static final String IS_NIGHTMODE = "night_mode";

    public static final String CURRENT_FRAGMENT_POS = "current_fragment";


    public static final int CARD_NIGHT = 0x00;

    public static final int CARD_DEFAULT = 0x09;


    //public static final int CARD_SAKURA = 0x1;
    public static final int CARD_HOPE = 0x2;
    public static final int CARD_STORM = 0x3;
    public static final int CARD_WOOD = 0x4;
    public static final int CARD_LIGHT = 0x5;
    public static final int CARD_THUNDER = 0x6;
    public static final int CARD_SAND = 0x7;
    public static final int CARD_FIREY = 0x8;

    public static final int TYPE_HOMEPAGE = 0x00;

    public static final int TYPE_SUBSCRIPTION = 0x01;

    public static final int TYPE_DISCOVERY = 0x02;

    public static final int TYPE_MINE = 0x03;


    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE);
    }

    public static void setTheme(Context mContext, int themeId) {
        getSharePreference(mContext).edit().putInt(CURRENT_THEME, themeId).commit();

    }

    public static int getTheme(Context context) {
        return getSharePreference(context).getInt(CURRENT_THEME, CARD_DEFAULT);
    }

    public static void setNightMode(Context mContext, boolean isNightMode) {
        getSharePreference(mContext).edit().putBoolean(IS_NIGHTMODE, isNightMode).commit();
    }

    public static boolean isNightMode(Context mContext) {
        return getSharePreference(mContext).getBoolean(IS_NIGHTMODE, false);
        //当前是否是夜间模式
    }


    public static boolean isDefaultTheme(Context context) {
        return getTheme(context) == CARD_DEFAULT;
    }

    public static String getThemeName(Context mContext) {
        int currentTheme = getTheme(mContext);
        switch (currentTheme) {
            case CARD_DEFAULT:
                return "DEFAULT";
            case CARD_NIGHT:
                return "THE NIGHT";
//            case CARD_SAKURA:
//                return "THE SAKURA";
            case CARD_STORM:
                return "THE STORM";
            case CARD_WOOD:
                return "THE WOOD";
            case CARD_LIGHT:
                return "THE LIGHT";
            case CARD_HOPE:
                return "THE HOPE";
            case CARD_THUNDER:
                return "THE THUNDER";
            case CARD_SAND:
                return "THE SAND";
            case CARD_FIREY:
                return "THE FIREY";

        }
        return "THR RETURN";
    }

    public static void setCurrentFragmentPos(Context mContext, int currentPos) {
        getSharePreference(mContext).edit().putInt(CURRENT_FRAGMENT_POS, currentPos).commit();
    }

    public static int getCurrentFragmentPost(Context mContext) {
        return getSharePreference(mContext).getInt(CURRENT_FRAGMENT_POS, 0);
    }


}
