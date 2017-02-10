package shinerich.com.stylemodel.bean;

/**
 * 分享bean
 *
 * @author hunk
 */
public class ShareBean {

    private String type = "1"; // 分享类型 1.文章
    private String title = ""; // 标题
    private String content = ""; // 内容
    private String clickUrl = ""; // 点击URL
    private String imageUrl = ""; // 图片

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

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShareBean{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", clickUrl='" + clickUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
