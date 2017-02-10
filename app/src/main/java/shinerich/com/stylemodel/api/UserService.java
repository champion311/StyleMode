package shinerich.com.stylemodel.api;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import shinerich.com.stylemodel.bean.AccountSetting;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.BindState;
import shinerich.com.stylemodel.bean.CommentDetail;
import shinerich.com.stylemodel.bean.MessageReply;
import shinerich.com.stylemodel.bean.MyCollect;
import shinerich.com.stylemodel.bean.MyComment;
import shinerich.com.stylemodel.bean.MyReply;
import shinerich.com.stylemodel.bean.SystemMessage;
import shinerich.com.stylemodel.bean.UserInfo;

/**
 * UserService
 *
 * @author hunk
 */
public interface UserService {

    //登录
    @FormUrlEncoded
    @POST("app/user/login")
    Call<BaseResponse<UserInfo>> login(@Field("mobile") String mobile,
                                       @Field("password") String password, @Field("type") int type,
                                       @Field("id") int id);

    //手机号是否注册
    @GET("app/user/Isexists")
    Call<BaseResponse<String>> isExistsMobile(@Query("mobile") String mobile);

    //注册
    @FormUrlEncoded
    @POST("app/user/register")
    Call<BaseResponse<UserInfo>> registerUser(@Field("mobile") String mobile, @Field("password") String password, @Field("rsource") int rsource,
                                              @Field("type") int type, @Field("id") int id);

    //第三方登录
    @FormUrlEncoded
    @POST("app/user/oauthlogin1")
    Call<BaseResponse<UserInfo>> oauthLogin(@FieldMap Map<String, String> params);

    //发送验证码
    @FormUrlEncoded
    @POST("app/user/Sendcode")
    Call<BaseResponse<String>> sendCode(@Field("mobile") String mobile, @Field("type") int type);

    //验证验证码
    @FormUrlEncoded
    @POST("app/user/verifycode")
    Call<BaseResponse<String>> verifyCode(@Field("mobile") String mobile, @Field("code") String code,
                                          @Field("type") int type);

    //重置密码
    @FormUrlEncoded
    @POST("app/user/mobileresetpwd")
    Call<BaseResponse<String>> resetPassword(@Field("mobile") String mobile,
                                             @Field("password") String password);

    //获取用户信息
    @GET("app/user/userInfo")
    Call<BaseResponse<UserInfo>> getUserInfo(@Query("uid") String uid, @Query("key") String key);


    //修改用户信息(含头像)
    @Multipart
    @POST("app/user/Updinfo")
    Call<BaseResponse<UserInfo>> modifyUserInfo(@PartMap Map<String, RequestBody> params, @Part("avatar\"; filename=\"avatar.jpg") RequestBody avatar);

    //反馈意见
    @FormUrlEncoded
    @POST("app/user/feedback")
    Call<BaseResponse<String>> feedback(@FieldMap Map<String, String> params);

    //我的收藏
    @GET("app/user/mycollect")
    Call<BaseResponse<List<MyCollect>>> getMyCollect(@Query("uid") String uid, @Query("key") String key,
                                                     @Query("page") int page);

    //系统消息
    @GET("app/user/Mysystem")
    Call<BaseResponse<List<SystemMessage>>> getMySystem(@Query("uid") String uid, @Query("key") String key,
                                                        @Query("page") int page);

    //我的评论
    @GET("app/user/mycomment")
    Call<BaseResponse<List<MyComment>>> getMyComment(@Query("uid") String uid, @Query("key") String key,
                                                     @Query("page") int page);

    //我收到的回复
    @GET("app/user/MyReply")
    Call<BaseResponse<List<MyReply>>> getMyReply(@Query("uid") String uid, @Query("key") String key,
                                                 @Query("page") int page);

    //已读状态修改
    @FormUrlEncoded
    @POST("app/user/read")
    Call<BaseResponse<String>> modifyRead(@Field("uid") String uid, @Field("key") String key,
                                          @Field("id") String id);

    //消息的未读数量
    @GET("app/user/num")
    Call<BaseResponse<String>> getUserMsgNum(@Query("uid") String uid, @Query("key") String key);

    //添加回复
    @FormUrlEncoded
    @POST("app/content/addreply")
    Call<BaseResponse<List<MessageReply>>> addReply(@FieldMap Map<String, String> params);

    //评论详情
    @GET("app/content/commentdetail")
    Call<BaseResponse<CommentDetail>> commentDetail(@Query("comment_id") String comment_id);

    //账号设置
    @GET("app/user/setting")
    Call<BaseResponse<AccountSetting>> userSetting(@Query("uid") String uid, @Query("key") String key);

    //解除绑定
    @FormUrlEncoded
    @POST("app/user/unbind")
    Call<BaseResponse<String>> unbind(@Field("uid") String uid, @Field("key") String key, @Field("platform") String platform);

    //绑定第三方
    @FormUrlEncoded
    @POST("app/user/bind")
    Call<BaseResponse<BindState>> bind(@FieldMap Map<String, String> params);
}
