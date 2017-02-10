package shinerich.com.stylemodel.api;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import shinerich.com.stylemodel.bean.ArticleContentBean;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BloggerDetailBean;
import shinerich.com.stylemodel.bean.BloggerInfoBean;
import shinerich.com.stylemodel.bean.BloggerIntroContentBean;
import shinerich.com.stylemodel.bean.BloggersContentInfoBean;
import shinerich.com.stylemodel.bean.ColumnItem;
import shinerich.com.stylemodel.bean.CommentResponseBean;
import shinerich.com.stylemodel.bean.ContentIndexBean;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.ImageAlbumBean;
import shinerich.com.stylemodel.bean.ListByCateBean;
import shinerich.com.stylemodel.bean.MySubscribe;
import shinerich.com.stylemodel.bean.RecommendData;
import shinerich.com.stylemodel.bean.ReplyBean;
import shinerich.com.stylemodel.bean.SubHomePageContentBean;
import shinerich.com.stylemodel.bean.UpdateBean;


/**
 * Created by Administrator on 2016/8/26.
 */
public interface ApiService {

    //分类导航
    @GET("app/content/nav")
    Observable<BaseResponse<List<ColumnItem>>> getNav();

    //内容列表
    @GET("app/content/index")
    Observable<BaseResponse<ContentIndexBean>>
    getIndexContent(@Query("cate_id") int cate_id, @Query("page") int page);

    //栏目列表
    @GET("app/content/listbycate")
    Observable<BaseResponse<ListByCateBean>>
    getListCate(@Query("page") int page, @QueryMap Map<String, String> options);

    //博主主页
    @GET("app/content/listbyblogger")
    Observable<BaseResponse<BloggerDetailBean>> getBloggerInfo(@Query("blogger_id") int
                                                                       blogger_id, @Query("page") int page);

    //博主主页
    @GET("app/content/listbyblogger")
    Observable<BaseResponse<BloggerIntroContentBean>> getBloggerInfo(@Query("blogger_id") int
                                                                             blogger_id, @Query("page") int page, @QueryMap Map<String, String> options);


    @GET("app/content/listbyeditor")
    Observable<BaseResponse<BloggerIntroContentBean>> getEditorInfo(@Query("editor_id") int
                                                                            blogger_id, @Query("page") int page, @QueryMap Map<String, String> options);


    //订阅管理--推荐内容
    @GET("app/subscribe/category")
    Observable<BaseResponse<List<RecommendData>>>
    getRecommendData(@Query("uid") String uid, @Query("key") String key);

    //订阅管理--热门博主
    @GET("app/subscribe/blogger")
    Observable<BaseResponse<List<BloggerInfoBean>>> getHotBloggers(@Query("uid") String uid, @Query("key") String key);

    //添加订阅
    @FormUrlEncoded
    @POST("app/subscribe/addsubscribe")
    Observable<BaseResponse<String>> addSubscripe(@Field("uid") String uid,
                                                  @Field("key") String key, @Field("subscribe_uid") String subscribe_uid, @Field("type") String type);

    //取消订阅
    @FormUrlEncoded
    @POST("app/subscribe/cancelsubscribe")
    Observable<BaseResponse<String>> cancelSubscripe(@Field("uid") String uid,
                                                     @Field("key") String key, @Field("subscribe_uid") String subscribe_uid, @Field("type") String type);


    //订阅首页
    @GET("app/subscribe/index")
    Observable<BaseResponse<BloggersContentInfoBean>> getBloggersIndex(@QueryMap Map<String, String> optionss);


    //我的订阅 //TODO
    @GET("app/subscribe/mysubscribe")
    Observable<BaseResponse<MySubscribe>> getMySubscribe(@Query("uid") String uid,
                                                         @Query("key") String key);

    //文章/视频/博文 详情页
    @GET("app/content/article")
    Observable<BaseResponse<ArticleContentBean>> getArticleBean(@QueryMap Map<String, String> maps,
                                                                @Query("id") String id, @Query("type") String type);

    //更多评论
    @GET("app/content/commentlist")
    Observable<BaseResponse<CommentResponseBean>> getCommentResponse(@QueryMap Map<String, String> maps);

    //添加评论
    @POST("app/content/addcomment")
    @FormUrlEncoded
    Observable<BaseResponse<Danmu>> addComment(@FieldMap Map<String, String> maps);

    //添加回复
    @POST("app/content/addreply")
    @FormUrlEncoded
    Observable<BaseResponse<List<ReplyBean>>> addReply(@FieldMap Map<String, String> maps);

    //评论点赞
    @POST("app/content/addpraise")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addPraise(@Field("uid") String uid,
                                               @Field("key") String key, @Field("comment_id") String comment_id);

    //评论取消点赞
    @POST("app/content/delpraise")
    @FormUrlEncoded
    Observable<BaseResponse<String>> delpraise(@Field("uid") String uid,
                                               @Field("key") String key, @Field("comment_id") String comment_id);

    //获取弹幕
    @GET("app/content/allcomment")
    Observable<BaseResponse<List<Danmu>>> getDanmuAllComents(@Query("id") String id, @Query("type") String type);


    //添加收藏
    @POST("app/content/collect")
    @FormUrlEncoded
    Observable<BaseResponse<String>> addCollect(@Field("uid") String uid,
                                                @Field("key") String key, @Field("id")
                                                        String id, @Field("type") String type);

    //取消收藏
    @POST("app/content/delcollect")
    @FormUrlEncoded
    Observable<BaseResponse<String>> delCollect(@Field("uid") String uid,
                                                @Field("key") String key, @Field("id")
                                                        String id, @Field("type") String type);

    // 图集详情
    @GET("app/content/album")
    Observable<BaseResponse<ImageAlbumBean>> getAlbum(@QueryMap Map<String, String> maps,
                                                      @Query("id") String id, @Query("type") String type);


    //订阅首页-内容列表
    @GET("app/subscribe/list")
    Observable<BaseResponse<List<SubHomePageContentBean>>>
    getSubList(@Query("uid") String uid, @Query("key") String key, @Query("page") int page);

    //版本更新
    @GET("app/ad/update")
    Observable<BaseResponse<UpdateBean>> updateApp(@Query("version") String version,
                                                   @Query("resource") int resource);





    //取消收藏


//    @GET("app/content/nav")
//    Call<BaseResponse<List<ColumnItem>>> getNavv();


//    @POST("{url}")
//    Observable<ResponseBody> executePost(
//            @Path("url") String url,
//            @QueryMap Map<String, String> maps);
//
//    //单张图片上传
//    @Multipart
//    @POST("{url}")
//    Observable<ResponseBody> upLoadFile(
//            @Path("url") String url,
//            @Part("image\"; filename=\"image.jpg") RequestBody avatar);
//
//
//    //多张图片上传
//    @POST("{url}")
//    Call<ResponseBody> uploadFiles(
//            @Path("url") String url,
//            @Part("filename") String description,
//            @PartMap() Map<String, RequestBody> maps);


}
