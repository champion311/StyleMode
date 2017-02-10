package shinerich.com.stylemodel.bean;

/**
 * 评论回复
 *
 * @author hunk
 */
public class CommentReply {

    private String id;   //评论Id
    private String comment_id;
    private String reply_id;
    private String content;
    private String user_id;
    private String addtime;
    private String nickname;
    private String usericon;
    private String reply_uid;
    private String reply_nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getReply_uid() {
        return reply_uid;
    }

    public void setReply_uid(String reply_uid) {
        this.reply_uid = reply_uid;
    }

    public String getReply_nickname() {
        return reply_nickname;
    }

    public void setReply_nickname(String reply_nickname) {
        this.reply_nickname = reply_nickname;
    }

    @Override
    public String toString() {
        return "CommmentReply{" +
                "id='" + id + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", reply_id='" + reply_id + '\'' +
                ", content='" + content + '\'' +
                ", user_id='" + user_id + '\'' +
                ", addtime='" + addtime + '\'' +
                ", nickname='" + nickname + '\'' +
                ", usericon='" + usericon + '\'' +
                ", reply_uid='" + reply_uid + '\'' +
                ", reply_nickname='" + reply_nickname + '\'' +
                '}';
    }
}
