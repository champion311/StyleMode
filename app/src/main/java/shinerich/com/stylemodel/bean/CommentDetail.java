package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * 评论详情
 *
 * @author hunk
 */
public class CommentDetail {
    private String id; //id
    private String content;
    private String user_id;
    private String addtime;
    private String usericon;
    private String nickname;
    private String outer_id;
    private String outer_type;
    private String outer_title;
    private List<CommentReply> reply;  //回复

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

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(String outer_id) {
        this.outer_id = outer_id;
    }

    public String getOuter_type() {
        return outer_type;
    }

    public void setOuter_type(String outer_type) {
        this.outer_type = outer_type;
    }

    public String getOuter_title() {
        return outer_title;
    }

    public void setOuter_title(String outer_title) {
        this.outer_title = outer_title;
    }

    public List<CommentReply> getReply() {
        return reply;
    }

    public void setReply(List<CommentReply> reply) {
        this.reply = reply;
    }

}
