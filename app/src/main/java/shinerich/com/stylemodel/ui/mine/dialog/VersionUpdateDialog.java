package shinerich.com.stylemodel.ui.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseDialog;
import shinerich.com.stylemodel.bean.UpdateBean;
import shinerich.com.stylemodel.common.DownApkTask;
import shinerich.com.stylemodel.common.GloableValues;
import shinerich.com.stylemodel.ui.mine.adapter.VersionUpdateAdapter;
import shinerich.com.stylemodel.utils.ApplicationUtils;
import shinerich.com.stylemodel.utils.CollectionsUtils;

/**
 * 版本更新-Dialog
 *
 * @author hunk
 */
public class VersionUpdateDialog extends BaseDialog implements View.OnClickListener {

    private UpdateBean updateBean;
    private ImageView iv_close;
    private List<String> datas = new ArrayList<>();
    private ListView lv_content;
    private TextView tv_version, tv_size;
    private VersionUpdateAdapter adapter;
    private ProgressBar pbar_progress;
    private FrameLayout fl_progress;
    private TextView tv_progress;
    private DownApkTask task;
    private boolean isDownloading = false;    //是否正在下载
    private boolean isCancel = true;


    public VersionUpdateDialog(Context context) {
        super(context);
    }


    public void setUpdateBean(UpdateBean updateBean) {
        this.updateBean = updateBean;
    }


    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(isCancel);
        setContentView(R.layout.dialog_version_update);

        // 初始化数据
        initData();
        //设置事件监听器
        setListener();
    }

    private void initData() {
        //初始化数据
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_size = (TextView) findViewById(R.id.tv_size);
        lv_content = (ListView) findViewById(R.id.lv_content);
        pbar_progress = (ProgressBar) findViewById(R.id.pbar_progress);
        fl_progress = (FrameLayout) findViewById(R.id.fl_progress);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        pbar_progress.setProgress(0);
        tv_progress.setText("立即更新");


        tv_version.setText(updateBean.getVersion());
        tv_size.setText(updateBean.getSize());
        if (!CollectionsUtils.isEmpty(updateBean.getContent())) {
            datas = updateBean.getContent();
            //设置数据
            adapter = new VersionUpdateAdapter(context, datas);
            lv_content.setAdapter(adapter);
        }
        

    }

    public void hideCloseBtn() {
        if (iv_close != null) {
            iv_close.setVisibility(View.GONE);
        }
    }

    public void setListener() {
        iv_close.setOnClickListener(this);
        fl_progress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //关闭
            case R.id.iv_close:
                dismiss();
                break;
            //更新进度
            case R.id.fl_progress:
                updateProgress();
                break;
        }

    }

    /**
     * 更新进度
     */
    public void updateProgress() {
        int progress = pbar_progress.getProgress();
        if (progress == 100) {
            String apkPath = task.getApkPath();
            ApplicationUtils.installApk(context, apkPath);
        } else {
            if (!isDownloading) {
                isDownloading = true;
                pbar_progress.setProgress(0);
                tv_progress.setText("正在更新");
                File dir = new File(GloableValues.BASE_PATH);
                task = new DownApkTask(pbar_progress, updateBean.getUrl(), dir);
                task.setOnCallbackListener(new DownApkTask.OnCallbackListener() {
                    @Override
                    public void onCompete() {
                        isDownloading = false;
                        tv_progress.setText("立即安装");
                    }
                });
                task.download();
            }
        }

    }

    /**
     * 取消
     */
    public void cancel() {
        if (task != null) {
            task.cancel();
        }
        dismiss();
    }

}
