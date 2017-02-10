package shinerich.com.stylemodel.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.utils.HBitmapUtils;
import shinerich.com.stylemodel.utils.SDCardUtils;

/**
 * 关于我们
 *
 * @author hunk
 */
public class AboutActivity extends SimpleActivity {


    @BindView(R.id.btn_declare)
    Button btn_declare;
    @BindView(R.id.iv_code)
    ImageView iv_code;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_about;

    }

    @Override
    protected void initEventAndData() {
        //初始化标题
        initTitle();
        //设置事件监听器
        setListener();
    }

    /**
     * 设置事件监听器
     */
    public void setListener() {
        iv_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iv_code.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(iv_code.getDrawingCache());
                iv_code.setDrawingCacheEnabled(false);
                String bitmapPath = SDCardUtils.getCameraPath();
                if (bitmap != null) {
                    File file = HBitmapUtils.saveBitmap(bitmap, bitmapPath, "my_code.png");
                    //更新图库
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath()));
                    sendBroadcast(intent);
                    showToast("保存相册成功");
                }

                return false;
            }
        });

    }


    @OnClick(value = {R.id.btn_declare})
    public void OnClick(View view) {
        switch (view.getId()) {
            //隐私声明
            case R.id.btn_declare:
                startIntent(this, PrivacyDeclareActivity.class);
                break;
        }
    }

    /*
    *  初始化标题
    */
    private void initTitle() {
        onMyBack();
        setMyTitle("关于我们");
    }

}
