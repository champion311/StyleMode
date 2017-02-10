package shinerich.com.stylemodel.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/26.
 */
public class NetWorkInterceptor implements Interceptor{

    //用来添加token


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        String token=null;//TODO 获取token
        if(token==null){
            return  chain.proceed(request);
        }
        Request authorised = request.newBuilder()
                .header("Authorization", token)//TODO 待修改
                .build();
        return chain.proceed(authorised);

    }
}


