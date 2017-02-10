package shinerich.com.stylemodel.ui.main.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.ui.widget.DanmakuView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseActivity;
import shinerich.com.stylemodel.bean.Danmu;
import shinerich.com.stylemodel.bean.ImageAlbumBean;
import shinerich.com.stylemodel.bean.ImageHideEvent;
import shinerich.com.stylemodel.bean.ShareBean;
import shinerich.com.stylemodel.common.ShareDialog;
import shinerich.com.stylemodel.engin.LoginUserProvider;
import shinerich.com.stylemodel.inject.component.ActivityComponent;
import shinerich.com.stylemodel.presenter.ImageAlbumPresenter;
import shinerich.com.stylemodel.presenter.contract.ImageAlbumContract;
import shinerich.com.stylemodel.ui.login.LoginSelectActivity;
import shinerich.com.stylemodel.ui.main.adapter.AlbumImageAdapter;
import shinerich.com.stylemodel.utils.DanmuController;
import shinerich.com.stylemodel.utils.KeyBoardUtils;
import shinerich.com.stylemodel.utils.ToastUtils;
import shinerich.com.stylemodel.widget.MyViewPager;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ImageListActivity extends BaseActivity<ImageAlbumPresenter>
        implements ImageAlbumContract.View,
        DanmuController.OnDanmuClickListener,
        AlbumImageAdapter.MatrixChangedListener {


    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.album_title)
    TextView albumTitle;
    @BindView(R.id.input_edit)
    EditText inputEdit;
    @BindView(R.id.comment_icon)
    ImageView commentIcon;
    @BindView(R.id.comment_number)
    TextView commentNumber;
    @BindView(R.id.collect_btn)
    ImageView collectBtn;
    @BindView(R.id.danmu_btn)
    ImageView danmuBtn;
    @BindView(R.id.share_btn)
    ImageView share_btn;
    @BindView(R.id.bottom_input_view)
    LinearLayout bottomInputView;
    @BindView(R.id.album_content_title)
    TextView albumContentTitle;
    @BindView(R.id.album_content)
    TextView albumContent;
    @BindView(R.id.bottom_wrapper)
    RelativeLayout bottomWrapper;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.danmakuView)
    DanmakuView danmakuView;
    @BindView(R.id.top_wrapper)
    RelativeLayout topWrapper;
    @BindView(R.id.bottom_line)
    View bottomLine;
    @BindView(R.id.mParentView)
    RelativeLayout mParentView;
    @BindView(R.id.comment_wrapper)
    RelativeLayout commentWrapper;
    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;

    private String id;
    private String type;


    private int currentPage;

    private DanmuController mDanmuControl;


    private boolean isCollected;

    private ShareBean shareBean;

    private ShareDialog shareDialog;

    private boolean isShowView = true;

    private boolean isShowDanmu = false;

    private List<Danmu> danmus;//弹幕数据

    @Override
    protected int getContentViewID() {
        return R.layout.activity_image_album;
    }

    @Override
    protected void injectDaggger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }


    @Override
    protected void initViewAndEvents() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(type)) {
            return;
        }
        mPresenter.loadData(id, type);
        mPresenter.loadDanmu(id, type);
        mDanmuControl.setDanmakuView(danmakuView);
        mDanmuControl.setOnDanmuClicklistener(this);
        //bottomWrapper.setAlpha(0.9f);
        mProgressBar.setVisibility(View.VISIBLE);


    }

    @Override
    public void setView(final ImageAlbumBean bean) {
        mProgressBar.setVisibility(View.GONE);
        albumTitle.setText("1/" + bean.getList().size());
        albumContentTitle.setText(bean.getTitle());
        AlbumImageAdapter adapter = new AlbumImageAdapter(this, bean.getList());
        adapter.setListener(this);
        mViewPager.setAdapter(adapter);
        commentNumber.setText(bean.getComment_num());
        albumContent.setText(bean.getDesc());

        isCollected = bean.getIs_collect().equals("1");
        collectBtn.setSelected(isCollected);
        if (isCollected) {
            collectBtn.setImageResource(R.drawable.collect_icon_selected);
        } else {
            collectBtn.setImageResource(R.drawable.collect_dark);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                albumContent.setText(bean.getList().get(position).getContent());
                albumTitle.setText((position + 1) + "/" + bean.getList().size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        danmuBtn.setSelected(isShowDanmu);

        MyViewPager myViewPager = (MyViewPager) mViewPager;
        shareBean = new ShareBean();
        shareBean.setClickUrl(bean.getShare_url());
        shareBean.setContent(bean.getTitle());
        shareBean.setTitle(bean.getTitle());
        shareBean.setImageUrl(bean.getThumb());
        shareBean.setType("1");
        shareDialog = new ShareDialog(this);
        shareDialog.setShareData(shareBean);
        if (mDanmuControl != null) {
            if (isShowDanmu) {
                mDanmuControl.show();
            } else {
                mDanmuControl.hide();
            }
        }


    }


    @Override
    public void showError() {
        topWrapper.setVisibility(View.GONE);
        bottomWrapper.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);



    }


    @OnClick({R.id.back_icon, R.id.share_btn,
            R.id.danmu_btn, R.id.comment_icon,
            R.id.collect_btn, R.id.comment_wrapper, R.id.input_edit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon:
                finish();
                break;
            case R.id.share_btn:
                //TODO
                if (shareDialog != null) {
                    shareDialog.show();
                }
                break;
            case R.id.danmu_btn:
                isShowDanmu = !isShowDanmu;
                if (isShowDanmu) {
                    mDanmuControl.show();
                } else {
                    mDanmuControl.hide();
                }
                danmuBtn.setSelected(isShowDanmu);
                break;
            case R.id.collect_btn:
                if (isCollected) {
                    mPresenter.removeCollect(id, type);
                } else {
                    mPresenter.addCollect(id, type);
                }

                break;
            case R.id.comment_wrapper:
            case R.id.comment_icon:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                startActivity(intent);

                break;
            case R.id.input_edit:
                if (!LoginUserProvider.currentStatus) {
                    notLoginAction();
                }
                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDanmuControl = new DanmuController(this);
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mDanmuControl.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDanmuControl.resume();
        if (LoginUserProvider.currentStatus) {
            inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        mPresenter.addComment(id, type, v.getText().toString());
                    }
                    inputEdit.clearFocus();
                    KeyBoardUtils.hideSoftKeyboard(ImageListActivity.this);
                    return false;
                }
            });
        } else {
            inputEdit.setFocusable(false);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDanmuControl.destroy();
        mDanmuControl = null;
    }

    @Override
    public void showDanmuView(List<Danmu> danmus) {
        //返回弹幕数据
        if (danmus != null && danmus.size() > 0) {
            this.danmus.addAll(danmus);
            mDanmuControl.addDanmuListTest(danmus);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDanmuControl.hide();
            }
        }, 500);
    }

    @Override
    public void addUserDanmu(Danmu danmu) {
        if (mDanmuControl != null) {
            IDanmakus idanmaks = danmakuView.getCurrentVisibleDanmakus();
            danmu.setFromLocal(true);
            mDanmuControl.addDanmu(danmu, -1);
            //mDanmuControl.addDanmu(danmu,);
            ToastUtils.show(this, "发送弹幕成功");
            KeyBoardUtils.hideSoftKeyboard(this);
        }
    }

    @Override
    public void setCollected(boolean hasCollected) {
        collectBtn.setSelected(hasCollected);
        if (hasCollected) {
            collectBtn.setImageResource(R.drawable.collect_icon_selected);
        } else {
            collectBtn.setImageResource(R.drawable.collect_dark);
        }

        isCollected = hasCollected;
    }

    @Override
    public void notLoginAction() {
        Intent intent = new Intent(this, LoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onDanmuClick(BaseDanmaku latest) {
        //ToastUtils.show(this, latest.userId + "");
    }

    /**
     *
     */
    public void showOrHideView(boolean isShow) {
        if (isShow) {
            mDanmuControl.show();
            bottomLine.setVisibility(View.VISIBLE);
        } else {
            bottomLine.setVisibility(View.GONE);
            mDanmuControl.hide();
        }
        if (!isShow) {
            //隐藏
            Animation bottomDisMiss = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    1f);
            bottomDisMiss.setDuration(500);
            bottomWrapper.startAnimation(bottomDisMiss);
            bottomDisMiss.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    bottomWrapper.setVisibility(View.GONE);

                }
            });
            Animation TopDimss = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    -1f);
            TopDimss.setDuration(500);
            topWrapper.startAnimation(TopDimss);
            TopDimss.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    topWrapper.setVisibility(View.GONE);
                }
            });

        } else {
            bottomWrapper.setVisibility(View.VISIBLE);
            Animation bottomShow = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
                    0f);
            bottomShow.setDuration(500);
            bottomWrapper.startAnimation(bottomShow);
            bottomShow.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                }
            });
            topWrapper.setVisibility(View.VISIBLE);
            Animation TopShow = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, 0.f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            TopShow.setDuration(500);
            topWrapper.startAnimation(TopShow);
            TopShow.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub

                }
            });

        }
    }

    @Override
    public void hideOrShowView(ImageHideEvent event) {
        isShowView = !isShowView;
        showOrHideView(isShowView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isShowView = !isShowView;
            showOrHideView(isShowView);
        }
        return super.onTouchEvent(event);
    }

    /**
     * PhotoView掉用监听
     */

    @Override
    public void changedCall() {
        if (isShowView) {
            isShowView = !isShowView;
            showOrHideView(isShowView);
        }
    }
}
