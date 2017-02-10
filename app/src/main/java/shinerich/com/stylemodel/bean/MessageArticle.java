package shinerich.com.stylemodel.bean;

/**
 * 消息文章
 *
 * @author hunk
 */
public class MessageArticle {


    private String id;
    private String type;        //类型 0文章1图集2视频3博文
    private String title;       //标题

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MessageArticle{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
