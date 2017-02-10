package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ContentIndexBean {

    //内容列表

    private List<FocusImageBean> focus;

    private List<ListContentBean> list;

    public List<FocusImageBean> getFocus() {
        return focus;
    }

    public void setFocus(List<FocusImageBean> focus) {
        this.focus = focus;
    }

    public List<ListContentBean> getList() {
        return list;
    }

    public void setList(List<ListContentBean> list) {
        this.list = list;
    }
}
