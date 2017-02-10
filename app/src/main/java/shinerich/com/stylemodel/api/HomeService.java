package shinerich.com.stylemodel.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.HomeAdv;
import shinerich.com.stylemodel.bean.UpdateBean;

/**
 * HomeService
 *
 * @author hunk
 */
public interface HomeService {

    //登录
    @GET("app/ad/ads")
    Call<BaseResponse<HomeAdv>> getAds();

    //版本更新
    @GET("app/ad/update")
    Call<BaseResponse<UpdateBean>> updateApp(@Query("version") String version,
                                             @Query("resource") int resource);
}
