package shinerich.com.stylemodel.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.DiscoveryHome;
import shinerich.com.stylemodel.bean.DiscoveryHotTitle;
import shinerich.com.stylemodel.bean.DiscoveryWords;

/**
 * DiscoveryService
 *
 * @author hunk
 */
public interface DiscoveryService {

    //大家都在看
    @GET("app/search/find")
    Call<BaseResponse<DiscoveryHome>> findHome(@Query("page") int page);

    //默认热词
    @GET("app/search/gethotwords")
    Call<BaseResponse<DiscoveryHotTitle>> getHotWords();

    //获取相关搜索提示
    @GET("app/search/getsuggestwords")
    Call<BaseResponse<List<DiscoveryWords>>> getSuggestWords(@Query("keywords") String keywords);


    //有结果
    @GET("app/search/index")
    Call<BaseResponse<DiscoveryHome>> searchIndex(@Query("page") int page, @Query("keywords") String keywords);

}
