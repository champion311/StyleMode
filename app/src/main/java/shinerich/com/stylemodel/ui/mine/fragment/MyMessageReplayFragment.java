package shinerich.com.stylemodel.ui.mine.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.UserService;
import shinerich.com.stylemodel.base.SimpleFragment;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.MessageReply;
import shinerich.com.stylemodel.bean.MyReply;
import shinerich.com.stylemodel.common.DataState;
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.mine.adapter.MyMessageReplyAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.utils.GlideUtils;
import shinerich.com.stylemodel.widget.CircleImageView;

/***
 * 收到的回复
 *
 * @ hunk
 */
public class MyMessageReplayFragment extends SimpleFragment implements XRecyclerView.LoadingListener, View.OnTouchListener {


    private List<MyReply> datas = new ArrayList<>();
    private MyReply replyData;
    private int page = 1;
    private MyMessageReplyAdapter adapter;
    private GlideUtils glideUtils = GlideUtils.getInstance();
    @BindView(R.id.rv_data)
    XRecyclerView rv_data;
    @BindView(R.id.view_empty)
    LinearLayout view_empty;
    @BindView(R.id.v_reply)
    LinearLayout v_reply;
    @BindView(R.id.rl_item)
    RelativeLayout rl_item;
    @BindView(R.id.et_reply)
    EditText et_reply;
    @BindView(R.id.iv_reply_head)
    CircleImageView iv_reply_head;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_message;
    }

    @Override
    protected void initEventAndData() {

        rv_data.setLayoutManager(new LinearLayoutManager(mContext));
        rv_data.setLoadingMoreEnabled(true);
        rv_data.setPullRefreshEnabled(true);
        adapter = new MyMessageReplyAdapter(mContext, this, datas);
        rv_data.setAdapter(adapter);
        rv_data.setLoadingListener(this);
        //设置头像
        if (getUser() != null) {
            glideUtils.setErrorHolder(R.drawable.user_img_def);
            glideUtils.setPlaceHolder(R.drawable.user_img_def);
            glideUtils.load(mContext, iv_reply_head, getUser().getUsericon());
        }


        //加载数据
        loadData(DataState.DATA_INTI);

        //设置事件监听器
        setListener();
    }


    /**
     * 设置事件监听器
     */
    public void setListener() {
        //触摸事件
        rv_data.setOnTouchListener(this);
        //Item点击事件
        adapter.setOnItemClickListener(new MyMessageReplyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                //隐藏
                v_reply.setVisibility(View.GONE);
            }

            @Override
            public void onReplyClick(View view, int position) {

                v_reply.setVisibility(View.VISIBLE);
                String nickname = datas.get(position).getReply().getNickname();
                et_reply.setHint("回复" + nickname);
                replyData = datas.get(position);

            }
        });

        //搜索框
        et_reply.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //点击搜索键
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String content = et_reply.getText().toString();

                    if (!TextUtils.isEmpty(content) && replyData != null) {
                        String commtent_id = replyData.getReply().getComment_id();
                        String reply_id = replyData.getReply().getReply_id();
                        String reply_uid = replyData.getReply().getReply_uid();

                        reply(commtent_id, reply_id, reply_uid, content);

                    } else {
                        showToast("请输入内容");
                    }
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //隐藏回复
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v_reply.setVisibility(View.GONE);
        }
        return false;
    }


    /**
     * 回复
     */
    public void reply(String comment_id, String reply_id, String reply_uid, String content) {

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        String uid = "", key = "";
        if (getUser() != null) {
            uid = getUser().getId();
            key = getUser().getKey();
        }

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("key", key);
        params.put("comment_id", comment_id);
        params.put("reply_id", reply_id);
        params.put("reply_uid", reply_uid);
        params.put("content", content);
        Call<BaseResponse<List<MessageReply>>> call = userService.addReply(params);
        call.enqueue(new Callback<BaseResponse<List<MessageReply>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MessageReply>>> call, Response<BaseResponse<List<MessageReply>>> response) {

                BaseResponse<List<MessageReply>> body = response.body();
                if (body.getCode() == 0) {
                    showToast(body.getMsg());
                    //置空
                    et_reply.getText().clear();
                } else {
                    showToast(body.getMsg());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<MessageReply>>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    /**
     * 加载数据
     */
    public void loadData(final DataState state) {

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        String uid = "", key = "";
        if (getUser() != null) {
            uid = getUser().getId();
            key = getUser().getKey();
        }
        Call<BaseResponse<List<MyReply>>> call;
        if (state == DataState.DATA_INTI || state == DataState.DATA_UPDATE) {
            page = 1;
            call = userService.getMyReply(uid, key, page);

        } else {
            call = userService.getMyReply(uid, key, page + 1);

        }
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<List<MyReply>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MyReply>>> call, Response<BaseResponse<List<MyReply>>> response) {
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
                BaseResponse<List<MyReply>> body = response.body();
                if (body.getCode() == 0) {

                    List<MyReply> list = body.getData();
                    if (state == DataState.DATA_INTI || state == DataState.DATA_UPDATE) {
                        datas.clear();
                    }

                    if (!CollectionsUtils.isEmpty(list)) {
                        datas.addAll(list);
                    }
                    adapter.notifyDataSetChanged();

                    if (state == DataState.DATA_INTI || state == DataState.DATA_UPDATE) {
                        if (!CollectionsUtils.isEmpty(list)) {
                            view_empty.setVisibility(View.GONE);
                            rv_data.setVisibility(View.VISIBLE);
                        } else {
                            view_empty.setVisibility(View.VISIBLE);
                            rv_data.setVisibility(View.GONE);
                        }
                    } else if (state == DataState.DATA_NEXT) {
                        if (CollectionsUtils.isEmpty(list)) {
                            rv_data.setLoadingMoreEnabled(false);
                        }
                    }

                    //下一页

                    if (state == DataState.DATA_NEXT) {
                        page++;
                    }


                } else if (body.getCode() == 200) {  //被踢

                    new RemoteLogin().remoteLoginToDo(mActivity, false);

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MyReply>>> call, final Throwable t) {
                t.printStackTrace();
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
            }
        });
    }


    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        loadData(DataState.DATA_UPDATE);
    }

    /***
     * 下一页
     */
    @Override
    public void onLoadMore() {

        loadData(DataState.DATA_NEXT);
    }

}
