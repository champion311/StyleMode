package shinerich.com.stylemodel.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseDialog;
import shinerich.com.stylemodel.bean.ShareBean;
import shinerich.com.stylemodel.utils.ApplicationUtils;
import shinerich.com.stylemodel.utils.ToastUtils;

/**
 * 分享Dialog
 *
 * @author hunk
 */
public class ShareDialog extends BaseDialog implements OnClickListener {

    private Context mContext;
    private ShareBean data;
    private String copyUrl = "";

    public ShareDialog(Context context) {
        super(context);
        this.mContext = context;
        initParams();

    }

    private void initParams() {
        Window window = getWindow();
        setContentView(R.layout.activity_share);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimShare);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);

    }


    /**
     * 设置拷贝Url
     *
     * @param url
     */
    public void setCopyUrl(String url) {
        this.copyUrl = url;
    }


    /**
     * 设置分享参数
     */
    public void setShareData(ShareBean data) {
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化数据
        initData();


    }


    /**
     * 初始化数据
     */
    private void initData() {
        findViewById(R.id.tv_friend).setOnClickListener(this);
        findViewById(R.id.tv_weixin).setOnClickListener(this);
        findViewById(R.id.tv_qq).setOnClickListener(this);
        findViewById(R.id.tv_zone).setOnClickListener(this);
        findViewById(R.id.tv_weibo).setOnClickListener(this);
        findViewById(R.id.tv_copy).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 微信好友
            case R.id.tv_weixin:
                toShare(SHARE_MEDIA.WEIXIN);
                break;
            // 微信朋友圈
            case R.id.tv_friend:
                toShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            // QQ好友
            case R.id.tv_qq:
                toShare(SHARE_MEDIA.QQ);
                break;
            // QQ空间
            case R.id.tv_zone:
                toShare(SHARE_MEDIA.QZONE);
                break;
            // 新浪微博
            case R.id.tv_weibo:
                toShare(SHARE_MEDIA.SINA);
                break;
            // 复制
            case R.id.tv_copy:
                if (!TextUtils.isEmpty(copyUrl)) {
                    ApplicationUtils.copy(mContext, copyUrl);
                    ToastUtils.show(mContext, "成功");
                } else {
                    ToastUtils.show(mContext, "请初始化链接");
                }
                break;
            // 关闭
            case R.id.iv_close:
                dismiss();
                break;
        }

    }

    /**
     * 去分享
     */

    public void toShare(SHARE_MEDIA share_MEDIA) {
        if (data == null) {
            ToastUtils.show(mContext, "请先初始化分享参数");
            return;
        }
        Activity activity = (Activity) mContext;
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        if (share_MEDIA != SHARE_MEDIA.SINA &&
                !mShareAPI.isInstall(activity, share_MEDIA)) {
            ToastUtils.show(activity, "请安装客服端");
            return;
        }
        ShareAction share = new ShareAction(activity);
        share.setPlatform(share_MEDIA);
        if (share_MEDIA == SHARE_MEDIA.SINA) {
            share.withText(data.getContent() + data.getClickUrl());
        } else {
            share.withTitle(data.getTitle());
            share.withText(data.getContent());
            share.withTargetUrl(data.getClickUrl());
        }
        if (!TextUtils.isEmpty(data.getImageUrl())) {
            share.withMedia(new UMImage(mContext, data.getImageUrl()));
        } else {
            share.withMedia(new UMImage(mContext, R.mipmap.ic_launcher));
        }

        share.setCallback(new UMShareListener() {

            @Override
            public void onResult(SHARE_MEDIA arg0) {

                ToastUtils.show(mContext, "分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA arg0, Throwable arg1) {
                ToastUtils.show(mContext, "分享错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA arg0) {
                ToastUtils.show(mContext, "分享取消");
            }
        });
        // 开始分享
        share.share();
        dismiss();
    }

}
