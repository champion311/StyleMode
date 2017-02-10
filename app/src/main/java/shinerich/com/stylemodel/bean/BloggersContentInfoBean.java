package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class BloggersContentInfoBean {

    private List<BloggerDetailBean> blogger;

    private List<ContentData> category;

    public List<BloggerDetailBean> getBlogger_data() {
        return blogger;
    }

    public void setBlogger_data(List<BloggerDetailBean> blogger) {
        this.blogger = blogger;
    }

    public List<ContentData> getCategory() {
        return category;
    }

    public void setCategory(List<ContentData> category) {
        this.category = category;
    }
}
