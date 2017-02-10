package shinerich.com.stylemodel.ui.mine;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.SimpleActivity;
import shinerich.com.stylemodel.ui.mine.adapter.MessagePageTabAdapter;

/**
 * 我的消息
 *
 * @author hunk
 */
public class MyMessageActivity extends SimpleActivity {

    private final static String[] titles = new String[]{"收到的回复", "我的评论", "系统消息"};
    private MessagePageTabAdapter mAdapter;
    @BindView(R.id.tlayout_msg)
    TabLayout tlayout_msg;
    @BindView(R.id.viewPager_msg)
    ViewPager viewPager_msg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;

    }

    @Override
    protected void initEventAndData() {
        //初始化标题
        initTitle();
        //初始化TabViewPager
        intTabViewPager();

    }


    /**
     * 初始化TabViewPager
     */
    private void intTabViewPager() {

        tlayout_msg.setTabMode(TabLayout.MODE_FIXED);
        tlayout_msg.addTab(tlayout_msg.newTab().setText(titles[0]));
        tlayout_msg.addTab(tlayout_msg.newTab().setText(titles[1]));
        tlayout_msg.addTab(tlayout_msg.newTab().setText(titles[2]));
        mAdapter = new MessagePageTabAdapter(getSupportFragmentManager(), titles);
        viewPager_msg.setAdapter(mAdapter);
        tlayout_msg.setupWithViewPager(viewPager_msg);
    }


    /**
     * 初始化标题
     */
    private void initTitle() {

        onMyBack();
        setMyTitle("我的消息");
    }


}
