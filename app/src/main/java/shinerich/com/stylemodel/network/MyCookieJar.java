package shinerich.com.stylemodel.network;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import shinerich.com.stylemodel.AppApplication;

/**
 * Created by Administrator on 2016/9/7.
 */
public class MyCookieJar implements CookieJar {

    private final PersistentCookieStore cookieStore = new PersistentCookieStore(AppApplication.getAppContext());


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}
