package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ImageListBean {

    private String id; // 125;,//图片id
    private String title; // wsw搜索;,//标题
    private String content; // 5暖色“shearling”夹克 今冬只需这一件！美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆美妆;,//描述
    private String thumb_origin; // http://s.stylemode.com/uploads/album/deal/2015/01/20150121175905_96618.jpg;,//原图
    private String thumb_small; // http://s.stylemode.com/uploads/album/640x640/2015/01/20150121175905_96618.jpg;//内容图

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumb_origin() {
        return thumb_origin;
    }

    public void setThumb_origin(String thumb_origin) {
        this.thumb_origin = thumb_origin;
    }

    public String getThumb_small() {
        return thumb_small;
    }

    public void setThumb_small(String thumb_small) {
        this.thumb_small = thumb_small;
    }
}
