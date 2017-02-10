package shinerich.com.stylemodel.ui.discovery.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bilibili.magicasakura.widgets.TintLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.DiscoveryService;
import shinerich.com.stylemodel.base.SimpleFragment;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.DiscoveryHome;
import shinerich.com.stylemodel.bean.DiscoveryItem;
import shinerich.com.stylemodel.bean.NightModeEvent;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.discovery.SearchListActivity;
import shinerich.com.stylemodel.ui.discovery.adapter.DiscoveryHomeAdapter;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.utils.RxBus;
import shinerich.com.stylemodel.utils.RxUtils;
import shinerich.com.stylemodel.utils.ThemeHelper;

/**
 * 发现-Fragment
 *
 * @author hunk
 */
public class DiscoveryFragment extends SimpleFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.mParentView)
    TintLinearLayout mParentView;
    private DiscoveryHomeAdapter adapter;
    private List<DiscoveryItem> datas = new ArrayList<DiscoveryItem>();
    private int page = 1;                  //页数
    @BindView(R.id.lv_discovery)
    ListView lv_discovery;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.ll_footer_refresh)
    LinearLayout ll_footer_refresh;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initEventAndData() {

        adapter = new DiscoveryHomeAdapter(mContext, datas);
        lv_discovery.setAdapter(adapter);
        lv_discovery.setOnItemClickListener(this);

        //加载数据
        loadData();
        if (Build.VERSION.SDK_INT <= 19) {
            RxBus.getInstance().toObservable(NightModeEvent.class).
                    compose(RxUtils.<NightModeEvent>rxSchedulerHelp()).subscribe(new Action1<NightModeEvent>() {
                @Override
                public void call(NightModeEvent event) {
                    if (ThemeHelper.isNightMode(getActivity())) {
                        mParentView.setBackgroundColor(Color.parseColor("#3c3c3d"));
                    } else {
                        mParentView.setBackgroundColor(Color.WHITE);
                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            });
        }

    }


    @OnClick(value = {R.id.ll_search, R.id.ll_footer_refresh})
    public void OnClick(View view) {
        switch (view.getId()) {
            //搜索
            case R.id.ll_search:
                startIntent(mContext, SearchListActivity.class);
                break;
            //刷新
            case R.id.ll_footer_refresh:
                loadData();
                break;
        }
    }

    /**
     * 加载数据
     */
    public void loadData() {

        RetrofitClient client = RetrofitClient.getInstance();
        DiscoveryService discoveryService = client.create(DiscoveryService.class);
        Call<BaseResponse<DiscoveryHome>> call = discoveryService.findHome(page);
        if (page == 1) {
            hideProgressDialog();
        } else {
            showProgressDialog();
        }

        call.enqueue(new Callback<BaseResponse<DiscoveryHome>>() {
            @Override
            public void onResponse(Call<BaseResponse<DiscoveryHome>> call, Response<BaseResponse<DiscoveryHome>> response) {
                hideProgressDialog();
                final BaseResponse<DiscoveryHome> body = response.body();
                if (body != null) {
                    if (body.getCode() == 0) {
                        DiscoveryHome index = body.getData();

                        List<DiscoveryItem> list = index.getList();
                        datas.clear();
                        datas.addAll(list);
                        adapter.notifyDataSetChanged();

                        //下一页
                        page = index.getPage();
                    } else {
                        showToast(body.getMsg());
                    }
                }


            }

            @Override
            public void onFailure(Call<BaseResponse<DiscoveryHome>> call, final Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //去详情页
        DiscoveryItem data = datas.get(position);
        String aid = data.getId();
        String type = data.getType();
        //内容类型（0,文章、1，图集、2，视频、3，博文）
        if ("0".equals(type)) {
            Intent intent = new Intent(mContext, ArticleContentActivity.class);
            intent.putExtra("id", aid);
            intent.putExtra("type", type);
            startActivity(intent);
        } else if ("1".equals(type)) {
            Intent intent = new Intent(mContext, ImageListActivity.class);
            intent.putExtra("id", aid);
            intent.putExtra("type", type);
            startActivity(intent);
        }

    }
}
