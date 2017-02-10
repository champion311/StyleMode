package shinerich.com.stylemodel.ui;

import android.view.View;
import android.webkit.WebView;

import butterknife.BindView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;


/**
 * 浏览器页面
 *
 * @author hunk
 */
public class WebViewActivity extends SimpleActivity {


    private String title;
    private String url;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;

    }

    @Override
    protected void initEventAndData() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");

        //初始标题
        initTitle();
        //初始化数据
        initData();

    }

    /**
     * 初始化数据
     */
    public void initData() {


        webView.getSettings().setJavaScriptEnabled(true);
        // 设置默认的编码格式
        webView.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        webView.loadUrl(url);
    }


    /***
     * 初始化标题
     */
    private void initTitle() {

        setMyTitle(title);
        onMyBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置返回OK
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        //设置返回OK
        setResult(RESULT_OK);
    }
}
