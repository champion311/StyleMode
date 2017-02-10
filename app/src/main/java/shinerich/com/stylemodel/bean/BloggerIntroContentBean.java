package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class BloggerIntroContentBean {

    private BloggerDetailBean blogger_data;

    private BloggerDetailBean editor_data;

    private List<ListContentBean> list;

    public BloggerDetailBean getBlogger_data() {
        return blogger_data;
    }

    public void setBlogger_data(BloggerDetailBean blogger_data) {
        this.blogger_data = blogger_data;
    }

    public List<ListContentBean> getList() {
        return list;
    }

    public void setList(List<ListContentBean> list) {
        this.list = list;
    }


    public BloggerDetailBean getEditor_data() {
        return editor_data;
    }

    public void setEditor_data(BloggerDetailBean editor_data) {
        this.editor_data = editor_data;
    }
}
