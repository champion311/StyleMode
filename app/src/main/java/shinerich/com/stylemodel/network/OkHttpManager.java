package shinerich.com.stylemodel.network;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import shinerich.com.stylemodel.AppApplication;
import shinerich.com.stylemodel.constant.AppConstants;

/**
 * Created by Administrator on 2016/8/26.
 */
public class OkHttpManager {

    private static OkHttpClient client;

    public static OkHttpClient getOkHttpClient() {
        if (client != null) {
            return client;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //if (true) {
        //AppConstants.sDebug
        //信息拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //设置 Debug Log 模式
        builder.addInterceptor(interceptor);
        //}
        builder.addInterceptor(new CacheInterceptor());
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        File cacheFile = new File(AppApplication.getAppContext().getExternalCacheDir(), "cacheDir");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        builder.cache(cache);
        // builder.retryOnConnectionFailure(true);
        //builder.addInterceptor(new NetWorkInterceptor());
        client = builder.build();
        //Cache
        builder.cookieJar(new MyCookieJar());
        return client;

    }


}
