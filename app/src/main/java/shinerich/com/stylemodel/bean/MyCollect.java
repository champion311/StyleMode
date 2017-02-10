package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 我的收藏
 *
 * @author hunk
 */
public class MyCollect implements Serializable {


    private String id;
    private String title;       //
    private String type;        //内容类型（0,文章、1，图集、2，视频、3，博文）
    private String thumb;
    private String cate_name;      //栏目名称
    private String cate_thumb;     //栏目图标
    private String cate_second_id;  //栏目id


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

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getCate_thumb() {
        return cate_thumb;
    }

    public void setCate_thumb(String cate_thumb) {
        this.cate_thumb = cate_thumb;
    }

    public String getCate_second_id() {
        return cate_second_id;
    }

    public void setCate_second_id(String cate_second_id) {
        this.cate_second_id = cate_second_id;
    }

    @Override
    public String toString() {
        return "MyCollect{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", thumb='" + thumb + '\'' +
                ", cate_name='" + cate_name + '\'' +
                ", cate_thumb='" + cate_thumb + '\'' +
                ", cate_second_id='" + cate_second_id + '\'' +
                '}';
    }
}
