package shinerich.com.stylemodel.network;

import android.text.TextUtils;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import shinerich.com.stylemodel.BuildConfig;
import shinerich.com.stylemodel.api.ApiService;

/**
 * Created by Administrator on 2016/8/26.
 */
public class RetrofitClient {


    public static String BASE_URL = "http://www.stylemode.com/";
    public static RetrofitClient instance;
    private ApiService apiService;
    private Retrofit retrofit;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static RetrofitClient getInstance(String url) {
        instance = new RetrofitClient(url);
        return instance;
    }


    //getRetrofitClient
    public RetrofitClient() {
        this(null);
    }


    public RetrofitClient(String url) {
        if (!TextUtils.isEmpty(url)) {
            BASE_URL = url;
        }
        Gson gson = GsonManager.getGson();
        OkHttpClient client = OkHttpManager.getOkHttpClient();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).
                        addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(client).
                        build();
        apiService = retrofit.create(ApiService.class);


    }

    public ApiService getApiService() {
        return apiService;
    }


    /**
     * 为了生成object对象
     *
     * @param clazz
     * @param retrofit
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> clazz, Retrofit retrofit) {
        return retrofit.create(clazz);
    }

    /**
     * 获取一个SerVice
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}
