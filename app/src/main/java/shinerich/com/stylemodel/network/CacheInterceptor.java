package shinerich.com.stylemodel.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import shinerich.com.stylemodel.AppApplication;
import shinerich.com.stylemodel.utils.NetWorkUtils;

/**
 * Created by Administrator on 2016/8/26.
 */
public class CacheInterceptor implements Interceptor{


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response=chain.proceed(request);
        if(NetWorkUtils.getNetWorkState(AppApplication.getAppContext())!=-1){

            int maxAge = 60*60; // read from cache for 60 minute
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        //先判断网络，网络好的时候，移除header后添加haunch失效时间为1小时，网络未连接的情况下设置缓存时间为4周
        return response;
    }
}
