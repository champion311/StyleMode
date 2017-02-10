package shinerich.com.stylemodel.utils;

import java.util.Locale;

/**
 * StringUtils
 *
 * @author hunk
 */
public class StringUtils {

    private StringUtils() {

    }


    /**
     * 验证密码
     */
    public static boolean verityPassword(String password) {
        if (password == null || password.length() == 0) {

            return false;
        }
        String reg = "^[A-Za-z0-9_#@]{8,16}$";
        return password.matches(reg);
    }

    /**
     * 验证手机号
     */
    public static boolean verityMobile(String number) {
        if (number == null || number.length() == 0) {
            return false;
        }
        String reg = "^(86)?0?1\\d{10}$";
        return number.matches(reg);
    }

    /**
     * 验证邮箱
     */
    public static boolean verityEmail(String email) {
        if (email == null || email.length() == 0) {
            return false;
        }
        String reg = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]" +
                "@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
        return email.matches(reg);
    }

    /**
     * 判断是否为空
     */
    public static boolean isNull(String str) {
        boolean isOK = true;
        if (str != null && str.length() != 0) {
            if (!"null".equals(str.toLowerCase(Locale.getDefault()))) {
                isOK = false;
            }
        }
        return isOK;
    }


    /**
     * 检查字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean notNull(String str) {
        if (str != null && !"".equals(str)) {
            return true;
        } else {
            return false;
        }
    }


}
