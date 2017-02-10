package shinerich.com.stylemodel.ui.mine.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shinerich.com.stylemodel.R;
import shinerich.com.stylemodel.base.BaseDialog;
import shinerich.com.stylemodel.bean.CityModel;
import shinerich.com.stylemodel.utils.ScreenUtils;
import shinerich.com.stylemodel.widget.wheelview.WheelNewView;
import shinerich.com.stylemodel.widget.wheelview.adapter.ArrayWheelAdapter;

/**
 * 用户地址选择-Dialog
 *
 * @author hunk
 */
public class UserAddressSelectorDialog extends BaseDialog implements View.OnClickListener
        , WheelNewView.OnWheelItemChangeListener {


    private OnItemSelectedListener listener;
    private TextView btn_sure, btn_cancel;
    private WheelNewView wv_city;
    private List<CityModel> cityModels = new ArrayList<CityModel>();
    private String code = "", name = "";
    private ArrayWheelAdapter<String> adapter;

    /**
     *  初始化城市
     */ {
        cityModels.add(new CityModel("100", "北京"));
        cityModels.add(new CityModel("101", "天津"));
        cityModels.add(new CityModel("102", "上海"));
        cityModels.add(new CityModel("103", "重庆"));
        cityModels.add(new CityModel("104", "河北"));
        cityModels.add(new CityModel("105", "山西"));
        cityModels.add(new CityModel("106", "内蒙古"));
        cityModels.add(new CityModel("107", "辽宁"));
        cityModels.add(new CityModel("108", "吉林"));
        cityModels.add(new CityModel("109", "黑龙江"));
        cityModels.add(new CityModel("110", "江苏"));
        cityModels.add(new CityModel("111", "浙江"));
        cityModels.add(new CityModel("112", "安徽"));
        cityModels.add(new CityModel("113", "江西"));
        cityModels.add(new CityModel("114", "福建"));
        cityModels.add(new CityModel("115", "山东"));
        cityModels.add(new CityModel("116", "河南"));
        cityModels.add(new CityModel("117", "湖北"));
        cityModels.add(new CityModel("118", "湖南"));
        cityModels.add(new CityModel("119", "广东"));
        cityModels.add(new CityModel("120", "广西"));
        cityModels.add(new CityModel("121", "海南"));
        cityModels.add(new CityModel("122", "香港"));
        cityModels.add(new CityModel("123", "澳门"));
        cityModels.add(new CityModel("124", "四川"));
        cityModels.add(new CityModel("125", "云南"));
        cityModels.add(new CityModel("126", "西藏"));
        cityModels.add(new CityModel("127", "贵州"));
        cityModels.add(new CityModel("128", "陕西"));
        cityModels.add(new CityModel("129", "甘肃"));
        cityModels.add(new CityModel("130", "青海"));
        cityModels.add(new CityModel("131", "宁夏"));
        cityModels.add(new CityModel("134", "新疆"));
    }

    public UserAddressSelectorDialog(Context context) {
        super(context);

        initParams();
    }

    /**
     * 获取名字
     */
    public String getCityName(String code) {
        String name = "";
        for (CityModel model : cityModels) {
            if (model.getId().equals(code)) {
                name = model.getName();
                break;
            }
        }
        return name;

    }



    /***
     * 初始化参数
     */
    private void initParams() {
        Window window = getWindow();
        setContentView(R.layout.dialog_user_address);
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

    private void initData() {
        //初始化View
        btn_sure = (TextView) findViewById(R.id.btn_confirm);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        wv_city = (WheelNewView) findViewById(R.id.wv_city);
        //wv_city.setBackgroundColor();

        //设置数据源
        String[] citys = new String[cityModels.size()];
        for (int i = 0; i < cityModels.size(); i++) {
            citys[i] = cityModels.get(i).getName();
        }
        code = cityModels.get(0).getId();
        name = cityModels.get(0).getName();

        wv_city.setLightBar(false);
        wv_city.setCurrentItem(0);
        adapter = new ArrayWheelAdapter<String>(context, citys);
        wv_city.setAdapter(adapter);
        wv_city.setOnWheelItemChangeListener(this);
        btn_sure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (listener != null) {
                    listener.onSelected(code, name);
                }
                this.dismiss();
                break;
            case R.id.btn_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }


    @Override
    public void onSelectedItemChanged(WheelNewView wheel, int lastpos, int newpos) {

        name = cityModels.get(newpos).getName();
        code = cityModels.get(newpos).getId();
    }

    /**
     * 条目选择
     */
    public interface OnItemSelectedListener {
        void onSelected(String code, String value);
    }


    /**
     * 设置监听事件
     */
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
