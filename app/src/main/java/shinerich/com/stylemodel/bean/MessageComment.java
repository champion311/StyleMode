package shinerich.com.stylemodel.bean;

/**
 * 消息评论
 *
 * @author hunk
 */
public class MessageComment {


    private String id;
    private String content;     //内容
    private String addtime;     //评论时间


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "MessageComment{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
