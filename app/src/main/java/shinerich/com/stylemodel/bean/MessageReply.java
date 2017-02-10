package shinerich.com.stylemodel.bean;

/**
 * 消息回复
 *
 * @author hunk
 */
public class MessageReply {


    private String id;                  //id
    private String comment_id;          //主评论id
    private String content;             //内容
    private String addtime;             //添加的时间
    private String reply_id;            //回复的评论id
    private String reply_uid;           //回复的评论的用户id
    private String user_id;             //用户id
    private String nickname;            //nickname
    private String usericon;            ////用户头像


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

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_uid() {
        return reply_uid;
    }

    public void setReply_uid(String reply_uid) {
        this.reply_uid = reply_uid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    @Override
    public String toString() {
        return "MessageReply{" +
                "id='" + id + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", content='" + content + '\'' +
                ", addtime='" + addtime + '\'' +
                ", reply_id='" + reply_id + '\'' +
                ", reply_uid='" + reply_uid + '\'' +
                ", user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", usericon='" + usericon + '\'' +
                '}';
    }
}
