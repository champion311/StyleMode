package shinerich.com.stylemodel.ui.discovery;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.api.DiscoveryService;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.bean.BaseResponse;
import shinerich.com.stylemodel.bean.DiscoveryHome;
import shinerich.com.stylemodel.bean.DiscoveryHotTitle;
import shinerich.com.stylemodel.bean.DiscoveryItem;
import shinerich.com.stylemodel.bean.DiscoveryWords;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.network.RetrofitClient;
import shinerich.com.stylemodel.ui.discovery.adapter.DiscoverySearchHistoryAdapter;
import shinerich.com.stylemodel.ui.discovery.adapter.DiscoverySearchListAdapter;
import shinerich.com.stylemodel.ui.discovery.adapter.DiscoverySearchWordsAdapter;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.utils.CollectionsUtils;
import shinerich.com.stylemodel.widget.MultipleTextViewGroup;

/**
 * 搜索列表
 *
 * @author hunk
 */
public class SearchListActivity extends SimpleActivity implements XRecyclerView.LoadingListener {

    //热门关键词
    private List<String> hotTitles = new ArrayList<>();
    //结果数据
    private List<DiscoveryItem> datas = new ArrayList<>();
    //历史数据
    private List<String> historyDatas = GloableValues.historyDatas;
    //是否去模糊搜索
    private boolean isGoHotSuggestwords = true;
    //关键字列表
    private List<DiscoveryWords> wordsDatas = new ArrayList<>();
    private DiscoverySearchListAdapter listAdapter;
    private DiscoverySearchHistoryAdapter historyAdapter;
    private DiscoverySearchWordsAdapter wordsAdapter;

    private int page = 1;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.mtv_hot)
    MultipleTextViewGroup mtv_hot;
    @BindView(R.id.et_words)
    EditText et_words;
    @BindView(R.id.ll_hot)
    LinearLayout ll_hot;
    @BindView(R.id.rv_data)
    XRecyclerView rv_data;
    @BindView(R.id.lv_history)
    ListView lv_history;
    @BindView(R.id.lv_words)
    ListView lv_words;
    @BindView(R.id.ll_history_clean)
    LinearLayout ll_history_clean;
    @BindView(R.id.v_history)
    View v_history;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.view_empty)
    LinearLayout view_empty;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_list;
    }


    @Override
    protected void initEventAndData() {
        //初始化数据
        initData();
        //设置点击事件
        setListener();

    }

    /***
     * 初始化数据
     */
    private void initData() {


        //搜索历史
        historyAdapter = new DiscoverySearchHistoryAdapter(mContext, historyDatas);
        lv_history.setAdapter(historyAdapter);
        //刷新历史视图
        refreshHistoryView();
        //热门关键词
        getHotwords();

        //关键字列表
        wordsAdapter = new DiscoverySearchWordsAdapter(this, wordsDatas);
        lv_words.setAdapter(wordsAdapter);


        //设置数据
        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setLoadingMoreEnabled(true);
        rv_data.setPullRefreshEnabled(true);

        listAdapter = new DiscoverySearchListAdapter(mContext, datas);
        rv_data.setAdapter(listAdapter);
        rv_data.setLoadingListener(this);


    }

    /**
     * 热门关键词
     */
    public void getHotwords() {

        RetrofitClient client = RetrofitClient.getInstance();
        DiscoveryService discoveryService = client.create(DiscoveryService.class);
        Call<BaseResponse<DiscoveryHotTitle>> call = discoveryService.getHotWords();
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<DiscoveryHotTitle>>() {
            @Override
            public void onResponse(Call<BaseResponse<DiscoveryHotTitle>> call, Response<BaseResponse<DiscoveryHotTitle>> response) {
                hideProgressDialog();
                BaseResponse<DiscoveryHotTitle> body = response.body();
                if (body.getCode() == 0) {
                    hotTitles = body.getData().getValue();
                    mtv_hot.setTextViews(hotTitles);

                    tv_hot.setVisibility(View.VISIBLE);
                    mtv_hot.setVisibility(View.VISIBLE);


                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DiscoveryHotTitle>> call, final Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });
    }

    /**
     * 设置点击事件
     */
    private void setListener() {

        // 热门关键词
        mtv_hot.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {

            @Override
            public void onMultipleTVItemClick(View view, int position) {

                String keywords = hotTitles.get(position);
                et_words.getText().clear();
                et_words.append(keywords);

                isGoHotSuggestwords = false;
                //设置当前页
                page = 1;
                rv_data.setLoadingMoreEnabled(true);
                //添加历史
                addHistorywords(keywords);
                //刷新历史视图
                refreshHistoryView();


                //隐藏
                ll_hot.setVisibility(View.GONE);
                lv_words.setVisibility(View.GONE);
                view_empty.setVisibility(View.GONE);
                rv_data.setVisibility(View.VISIBLE);

                //搜索数据
                searchData(keywords);


            }
        });


        //搜索框文本改变
        et_words.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keywords = s.toString();
                if (TextUtils.isEmpty(keywords)) {
                    //隐藏
                    lv_words.setVisibility(View.GONE);
                    view_empty.setVisibility(View.GONE);
                    ll_hot.setVisibility(View.VISIBLE);
                    rv_data.setVisibility(View.GONE);

                    page = 1;
                    rv_data.setLoadingMoreEnabled(true);
                    isGoHotSuggestwords = true;

                } else {
                    if (isGoHotSuggestwords) {
                        //隐藏
                        ll_hot.setVisibility(View.GONE);
                        rv_data.setVisibility(View.GONE);
                        view_empty.setVisibility(View.GONE);
                        lv_words.setVisibility(View.VISIBLE);
                        //获取相关数据
                        getHotSuggestwords(keywords);
                    }
                }

            }
        });

        //搜索框
        et_words.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //点击搜索键
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {

                    String keywords = et_words.getText().toString();
                    if (!TextUtils.isEmpty(keywords)) {
                        //隐藏
                        lv_words.setVisibility(View.GONE);
                        ll_hot.setVisibility(View.GONE);
                        view_empty.setVisibility(View.GONE);
                        rv_data.setVisibility(View.VISIBLE);

                        page = 1;
                        rv_data.setLoadingMoreEnabled(true);
                        isGoHotSuggestwords = false;

                        //搜索数据
                        searchData(keywords);
                    }
                    return true;
                }

                return false;
            }
        });


        //历史记录
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String keywords = historyDatas.get(position);
                et_words.getText().clear();
                et_words.append(keywords);


                //添加历史
                addHistorywords(keywords);
                //刷新历史视图
                refreshHistoryView();
                //设置当前页
                page = 1;
                rv_data.setLoadingMoreEnabled(true);
                //隐藏
                ll_hot.setVisibility(View.GONE);
                view_empty.setVisibility(View.GONE);
                rv_data.setVisibility(View.VISIBLE);
                lv_words.setVisibility(View.GONE);

                //搜索数据
                searchData(keywords);


            }
        });

        //相似关键字
        lv_words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keywords = wordsDatas.get(position).getValue();
                historyDatas.add(keywords);
                //刷新历史视图
                refreshHistoryView();
                //设置当前索引
                page = 1;
                rv_data.setLoadingMoreEnabled(true);
                isGoHotSuggestwords = false;
                //隐藏
                ll_hot.setVisibility(View.GONE);
                lv_words.setVisibility(View.GONE);
                view_empty.setVisibility(View.GONE);
                rv_data.setVisibility(View.VISIBLE);

                //开始搜索
                searchData(keywords);
            }
        });


        //搜索结果
        listAdapter.setOnItemClickListener(new DiscoverySearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //去详情页
                DiscoveryItem data = datas.get(position);
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
                DiscoveryItem data = datas.get(position);
                String type = data.getType();
                String cate_id = data.getCate_id();
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
        });


    }


    /**
     * 搜索数据
     */
    public void searchData(String keywords) {

        RetrofitClient client = RetrofitClient.getInstance();
        DiscoveryService discoveryService = client.create(DiscoveryService.class);
        Call<BaseResponse<DiscoveryHome>> call = discoveryService.searchIndex(page, keywords);
        showProgressDialog();
        call.enqueue(new Callback<BaseResponse<DiscoveryHome>>() {
            @Override
            public void onResponse(Call<BaseResponse<DiscoveryHome>> call, Response<BaseResponse<DiscoveryHome>> response) {
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
                BaseResponse<DiscoveryHome> body = response.body();
                if (body.getCode() == 0) {
                    List<DiscoveryItem> list = body.getData().getList();
                    if (page == 1) {
                        datas.clear();
                    }
                    if (!CollectionsUtils.isEmpty(list)) {
                        datas.addAll(list);
                    }
                    listAdapter.notifyDataSetChanged();
                    //下一页
                    page = body.getData().getPage();

                } else if (body.getCode() == 2) {  //无更多数据

                    rv_data.setLoadingMoreEnabled(false);
                    showToast(body.getMsg());

                } else if (body.getCode() == 3) {    //无搜索数据
                    datas.clear();
                    view_empty.setVisibility(View.VISIBLE);
                    listAdapter.notifyDataSetChanged();

                } else {
                    showToast(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DiscoveryHome>> call, final Throwable t) {
                t.printStackTrace();
                hideProgressDialog();
                rv_data.loadMoreComplete();
                rv_data.refreshComplete();
            }
        });
    }

    /**
     * 获取相关搜索
     */
    public void getHotSuggestwords(final String keywords) {

        RetrofitClient client = RetrofitClient.getInstance();
        DiscoveryService discoveryService = client.create(DiscoveryService.class);
        Call<BaseResponse<List<DiscoveryWords>>> call = discoveryService.getSuggestWords(keywords);
        call.enqueue(new Callback<BaseResponse<List<DiscoveryWords>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DiscoveryWords>>> call, Response<BaseResponse<List<DiscoveryWords>>> response) {

                BaseResponse<List<DiscoveryWords>> body = response.body();
                if (body.getCode() == 0) {

                    //设置数据源
                    List<DiscoveryWords> list = body.getData();
                    list.add(0, new DiscoveryWords(keywords));
                    wordsDatas.clear();
                    wordsDatas.addAll(list);
                    wordsAdapter.notifyDataSetChanged();


                } else {
                    showToast(body.getMsg());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<DiscoveryWords>>> call, final Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @OnClick(value = {R.id.iv_close, R.id.ll_history_clean})
    public void OnClick(View view) {
        switch (view.getId()) {
            //关闭
            case R.id.iv_close:
                finish();
                break;

            //清除历史
            case R.id.ll_history_clean:
                historyDatas.clear();
                historyAdapter.notifyDataSetChanged();
                //刷新历史视图
                refreshHistoryView();

                break;

        }
    }

    /**
     * 刷新历史视图
     */
    public void refreshHistoryView() {
        if (historyDatas.size() > 0) {
            lv_history.setVisibility(View.VISIBLE);
            ll_history_clean.setVisibility(View.VISIBLE);
            v_history.setVisibility(View.VISIBLE);
        } else {
            lv_history.setVisibility(View.GONE);
            ll_history_clean.setVisibility(View.GONE);
            v_history.setVisibility(View.GONE);
        }


    }

    /**
     * 添加历史关键字
     */
    public void addHistorywords(String keywords) {
        int index = -1;
        for (int i = 0; i < historyDatas.size(); i++) {
            if (historyDatas.get(i).equals(keywords)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            historyDatas.remove(index);
        }

        historyDatas.add(0, keywords);
        historyAdapter.notifyDataSetChanged();
    }


    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        String keywords = et_words.getText().toString().trim();
        searchData(keywords);
    }

    /***
     * 下一页
     */
    @Override
    public void onLoadMore() {
        String keywords = et_words.getText().toString().trim();
        searchData(keywords);
    }
}
