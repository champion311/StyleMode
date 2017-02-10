package shinerich.com.stylemodel.bean;

import android.support.annotation.Nullable;

import java.util.List;

import shinerich.com.stylemodel.bean.db.CateDataBean;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ListByCateBean {

    //栏目列表

    private CateDataBean cate_data;

    @Nullable
    private List<ListContentBean> list;

    public CateDataBean getCate_data() {
        return cate_data;
    }

    public void setCate_data(CateDataBean cate_data) {
        this.cate_data = cate_data;
    }

    public List<ListContentBean> getList() {
        return list;
    }

    public void setList(List<ListContentBean> list) {
        this.list = list;
    }
}
