package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 首页广告
 *
 * @author hunk
 */
public class HomeAdv implements Serializable {

    private String thumbnals;        //图片
    private String url;              //链接
    private String title;            //标题

    public String getThumbnals() {
        return thumbnals;
    }

    public void setThumbnals(String thumbnals) {
        this.thumbnals = thumbnals;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "HomeAdv{" +
                "thumbnals='" + thumbnals + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
