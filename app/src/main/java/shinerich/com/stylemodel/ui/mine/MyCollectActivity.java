package shinerich.com.stylemodel.ui.mine;

import android.content.Intent;
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
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.MyCollect;
import shinerich.com.stylemodel.common.DataState;
import shinerich.com.stylemodel.engin.RemoteLogin;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.ui.mine.adapter.MyCollectAdapter;
import shinerich.com.stylemodel.utils.CollectionsUtils;

/**
 * 我的收藏
 *
 * @author hunk
 */
public class MyCollectActivity extends SimpleActivity implements XRecyclerView.LoadingListener {

    private List<MyCollect> datas = new ArrayList<>();
    private int page = 1;
    private MyCollectAdapter adapter;
    @BindView(R.id.rv_data)
    XRecyclerView rv_data;
    @BindView(R.id.view_empty)
    LinearLayout view_empty;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collect;

    }

    @Override
    protected void initEventAndData() {
        //初始化标题
        initTitle();
        //初始化数据
        initData();
        //设置事件监听器
        setListener();


    }


    /**
     * 设置事件监听器
     */
    public void setListener() {

        adapter.setOnItemClickListener(new MyCollectAdapter.OnItemClickListener() {

                                           @Override
                                           public void onItemClick(View view, int position) {


                                               //去详情页
                                               MyCollect data = datas.get(position);
                                               String id = data.getId();
                                               String type = data.getType();
                                               //内容类型（0,文章、1，图集、2，视频、3，博文）
                                               if ("0".equals(type)) {
                                                   Intent intent = new Intent(mContext, ArticleContentActivity.class);
                                                   intent.putExtra("id", id);
                                                   intent.putExtra("type", type);
                                                   startActivity(intent);
                                               } else if ("1".equals(type)) {
                                                   Intent intent = new Intent(mContext, ImageListActivity.class);
                                                   intent.putExtra("id", id);
                                                   intent.putExtra("type", type);
                                                   startActivity(intent);
                                               }

                                           }

                                           @Override
                                           public void onCateClick(View view, int position) {
                                               //去栏目
                                               MyCollect data = datas.get(position);
                                               String type = data.getType();
                                               String cate_id = data.getCate_second_id();
                                               if ("3".equals(type)) {
                                                   Intent intent = new Intent(mContext, BloggerInfoActivity.class);
                                                   intent.putExtra("bloggers_id", cate_id);
                                                   startActivity(intent);
                                               } else {
                                                   Intent intent = new Intent(mContext, ColumnActivity.class);
                                                   intent.putExtra("cate_id", cate_id);
                                                   startActivity(intent);
                                               }
                                           }
                                       }

        );

    }


    /**
     * 初始化数据
     */
    public void initData() {
        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setLoadingMoreEnabled(true);
        rv_data.setPullRefreshEnabled(true);

        adapter = new MyCollectAdapter(mContext, datas);
        rv_data.setAdapter(adapter);
        rv_data.setLoadingListener(this);

        //加载数据
        loadData(DataState.DATA_INTI);

    }

    /**
     * 初始化标题
     */
    public void initTitle() {
        onMyBack();
        setMyTitle("我的收藏");
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
        Call<BaseResponse<List<MyCollect>>> call;
        if (state == DataState.DATA_INTI || state == DataState.DATA_UPDATE) {
            page = 1;
            call = userService.getMyCollect(uid, key, page);

        } else {
            call = userService.getMyCollect(uid, key, page + 1);

        }
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<List<MyCollect>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MyCollect>>> call, Response<BaseResponse<List<MyCollect>>> response) {
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
                BaseResponse<List<MyCollect>> body = response.body();
                if (body.getCode() == 0) {

                    List<MyCollect> list = body.getData();
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

                    new RemoteLogin().remoteLoginToDo(mContext, false);

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MyCollect>>> call, final Throwable t) {
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
