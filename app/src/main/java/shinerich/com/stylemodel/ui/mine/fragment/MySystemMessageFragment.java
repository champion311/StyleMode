package shinerich.com.stylemodel.ui.mine.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.UserService;
import shinerich.com.stylemodel.base.SimpleFragment;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.SystemMessage;
import shinerich.com.stylemodel.common.DataState;
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.mine.adapter.MySystemMessageAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;

/***
 * 系统消息
 *
 * @ hunk
 */
public class MySystemMessageFragment extends SimpleFragment implements XRecyclerView.LoadingListener {

    private List<SystemMessage> datas = new ArrayList<>();
    private int page = 1;
    private MySystemMessageAdapter adapter;

    @BindView(R.id.rv_data)
    XRecyclerView rv_data;
    @BindView(R.id.view_empty)
    LinearLayout view_empty;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_message_other;
    }

    @Override
    protected void initEventAndData() {

        rv_data.setLayoutManager(new LinearLayoutManager(mContext));
        rv_data.setLoadingMoreEnabled(true);
        rv_data.setPullRefreshEnabled(true);

        adapter = new MySystemMessageAdapter(mContext, datas);
        rv_data.setAdapter(adapter);
        rv_data.setLoadingListener(this);

        //加载数据
        loadData(DataState.DATA_INTI);

        //设置事件监听器
        setListener();
    }


    /**
     * 设置事件监听器
     */
    public void setListener() {

        adapter.setOnItemClickListener(new MySystemMessageAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                SystemMessage data = datas.get(position);
                //未读
                if ("1".equals(data.getStatus())) {
                    String id = datas.get(position).getId();
                    motifyRead(id, position);
                }


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
        Call<BaseResponse<List<SystemMessage>>> call;
        if (state == DataState.DATA_INTI || state == DataState.DATA_UPDATE) {
            page = 1;
            call = userService.getMySystem(uid, key, page);

        } else {
            call = userService.getMySystem(uid, key, page + 1);

        }
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<List<SystemMessage>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SystemMessage>>> call, Response<BaseResponse<List<SystemMessage>>> response) {
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
                BaseResponse<List<SystemMessage>> body = response.body();
                if (body.getCode() == 0) {

                    List<SystemMessage> list = body.getData();
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
            public void onFailure(Call<BaseResponse<List<SystemMessage>>> call, final Throwable t) {
                t.printStackTrace();
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
            }
        });
    }


    /**
     * 修改已读标志
     */
    public void motifyRead(String id, final int position) {

        RetrofitClient client = RetrofitClient.getInstance();
        UserService userService = client.create(UserService.class);
        String uid = "", key = "";
        if (getUser() != null) {
            uid = getUser().getId();
            key = getUser().getKey();
        }
        Call<BaseResponse<String>> call = userService.modifyRead(uid, key, id);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {

                BaseResponse<String> body = response.body();
                if (body.getCode() == 0) {
                    datas.get(position).setStatus("2");

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

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
