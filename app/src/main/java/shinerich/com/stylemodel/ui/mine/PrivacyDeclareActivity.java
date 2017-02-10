package shinerich.com.stylemodel.ui.mine;

import android.webkit.WebView;

import butterknife.BindView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.network.RetrofitClient;

/**
 * 隐私声明
 *
 * @author hunk
 */
public class PrivacyDeclareActivity extends SimpleActivity {

    private final static String url = RetrofitClient.BASE_URL + "app/page/privacy";
    @BindView(R.id.webView_privacy)
    WebView webView_privacy;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_privacy;

    }

    @Override
    protected void initEventAndData() {
        //初始标题
        initTitle();
        //初始化数据
        initData();

    }

    /**
     * 初始化数据
     */
    public void initData() {


        webView_privacy.getSettings().setJavaScriptEnabled(true);
        // 设置默认的编码格式
        webView_privacy.getSettings()
                .setDefaultTextEncodingName("UTF-8");
        webView_privacy.loadUrl(url);
    }


    /***
     * 初始化标题
     */
    private void initTitle() {
        onMyBack();
        setMyTitle("隐私声明");

    }
}
