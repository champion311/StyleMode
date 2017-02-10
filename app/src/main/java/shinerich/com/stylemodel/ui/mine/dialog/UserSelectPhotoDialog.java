package shinerich.com.stylemodel.ui.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseDialog;
import shinerich.com.stylemodel.utils.ScreenUtils;

/**
 * 相册选择Dialog
 *
 * @author hunk
 */
public class UserSelectPhotoDialog extends BaseDialog implements View.OnClickListener {

    private Context context;
    private OnItemSelectedListener listener;
    private TextView tv_take_photo;
    private TextView tv_take_album;
    private TextView tv_cancel;

    public UserSelectPhotoDialog(Context context) {
        super(context);
        this.context = context;

        initParams();
    }

    private void initParams() {
        Window window = getWindow();
        setContentView(R.layout.dialog_photo_selector);
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.DialogAnimMine);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = ScreenUtils.getScreenWidth(context);
        getWindow().setAttributes(lp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化数据
        initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //相册
            case R.id.tv_take_album:
                dismiss();
                if (listener != null) {
                    listener.onAlbumClick(v);
                }

                break;
            //相机
            case R.id.tv_take_photo:
                dismiss();
                if (listener != null) {
                    listener.onCameraClick(v);
                }

                break;
            //取消
            case R.id.tv_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }


    /**
     * 设置监听事件
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }


    /**
     * 初始化参数
     */
    public void initData() {


        tv_take_photo = (TextView)
                findViewById(R.id.tv_take_photo);
        tv_take_album = (TextView)
                findViewById(R.id.tv_take_album);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        tv_take_album.setOnClickListener(this);
    }


    public interface OnItemSelectedListener {
        //相机
        void onCameraClick(View view);

        //相册
        void onAlbumClick(View view);
    }


}
