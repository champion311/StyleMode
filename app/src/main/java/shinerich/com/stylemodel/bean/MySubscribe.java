package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class MySubscribe {

    private List<MySubBloggerBean> blogger;

    private List<MySubCateBean> category;

    public List<MySubBloggerBean> getBlogger() {
        return blogger;
    }

    public void setBlogger(List<MySubBloggerBean> blogger) {
        this.blogger = blogger;
    }

    public List<MySubCateBean> getCategory() {
        return category;
    }

    public void setCategory(List<MySubCateBean> category) {
        this.category = category;
    }
}
