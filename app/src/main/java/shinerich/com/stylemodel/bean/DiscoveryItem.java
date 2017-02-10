package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 发现条目
 *
 * @author hunk
 */
public class DiscoveryItem implements Serializable {

    private String id;
    private String title;           //标题
    private String type = "0";        //内容类型（0,文章、1，图集、2，视频、3，博文）
    private String thumb;
    private String c2name;        //栏目名称
    private String c2thumb;       //栏目图标
    private String cate_id;      //栏目id


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getC2name() {
        return c2name;
    }

    public void setC2name(String c2name) {
        this.c2name = c2name;
    }

    public String getC2thumb() {
        return c2thumb;
    }

    public void setC2thumb(String c2thumb) {
        this.c2thumb = c2thumb;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    @Override
    public String toString() {
        return "DiscoveryItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", thumb='" + thumb + '\'' +
                ", c2name='" + c2name + '\'' +
                ", c2thumb='" + c2thumb + '\'' +
                ", cate_id='" + cate_id + '\'' +
                '}';
    }
}
