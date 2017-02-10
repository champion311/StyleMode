package shinerich.com.stylemodel.ui.login;

import android.webkit.WebView;

import butterknife.BindView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.network.RetrofitClient;

/**
 * 用户协议
 *
 * @author hunk
 */
public class UserAgreementActivity extends SimpleActivity {

    private final static String url = RetrofitClient.BASE_URL + "app/page/protocal";

    @BindView(R.id.webView_user)
    WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_use_agreement;

    }

    @Override
    protected void initEventAndData() {
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
        setMyTitle("用户注册协议");
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
}
