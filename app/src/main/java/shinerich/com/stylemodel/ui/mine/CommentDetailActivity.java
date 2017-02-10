package shinerich.com.stylemodel.ui.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.UserService;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.CommentDetail;
import shinerich.com.stylemodel.bean.CommentReply;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.mine.adapter.CommentDetailAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.utils.HDateUtils;

/**
 * 评论详情
 *
 * @author hunk
 */
public class CommentDetailActivity extends SimpleActivity {

    private GlideUtils glideUtils = GlideUtils.getInstance();
    private CommentDetail detail;
    private String comment_id;        //评论id
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.iv_user_head)
    ImageView iv_user_head;
    @BindView(R.id.tv_article_title)
    TextView tv_article_title;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.lv_detail)
    ListView lv_detail;
    @BindView(R.id.btn_look)
    Button btn_look;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_comment_detail;
    }


    @Override
    protected void initEventAndData() {
        comment_id = getIntent().getStringExtra("comment_id");

        //初始化标题
        initTitle();
        //初始化数据
        initData();

    }

    /**
     * 初始化标题
     */
    public void initTitle() {
        onMyBack();
        setMyTitle("评论详情");
    }

    /**
     * 初始化数据
     */
    public void initData() {


        //加载数据
        loadData();

    }

    @OnClick(value = {R.id.btn_look})
    public void OnClick(View view) {

        switch (view.getId()) {
            //查看
            case R.id.btn_look:
                if (detail != null) {
                    //去详情页
                    Intent intent = new Intent(mContext, ArticleContentActivity.class);
                    intent.putExtra("id", detail.getOuter_id());
                    intent.putExtra("type", detail.getOuter_type());
                    startActivity(intent);
                }
                break;
        }


    }

    /**
     * 加载数据
     */
    public void loadData() {

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        Call<BaseResponse<CommentDetail>> call = userService.commentDetail(comment_id);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<CommentDetail>>() {
            @Override
            public void onResponse(Call<BaseResponse<CommentDetail>> call, Response<BaseResponse<CommentDetail>> response) {
                hideProgressDialog();
                BaseResponse<CommentDetail> body = response.body();
                if (body.getCode() == 0) {
                    ll_content.setVisibility(View.VISIBLE);
                    detail = body.getData();
                    if (detail != null) {
                        //评论信息
                        glideUtils.load(mContext, iv_user_head, detail.getUsericon(), R.drawable.user_img_def);
                        tv_name.setText(detail.getNickname());
                        if (!TextUtils.isEmpty(detail.getAddtime())) {
                            String strTime = HDateUtils.getDateTime(Long.parseLong(detail.getAddtime()));
                            tv_time.setText(strTime);
                        } else {
                            tv_time.setText("");
                        }

                        List<CommentReply> list = detail.getReply();
                        if (!CollectionsUtils.isEmpty(list)) {
                            lv_detail.setVisibility(View.VISIBLE);
                            CommentDetailAdapter adapter = new CommentDetailAdapter(mContext, list);
                            lv_detail.setAdapter(adapter);
                        } else {
                            lv_detail.setVisibility(View.GONE);
                        }

                        tv_article_title.setText(detail.getOuter_title());
                    }


                } else {
                    showToast(body.getMsg());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CommentDetail>> call, Throwable t) {
                hideProgressDialog();

            }
        });
    }
}
