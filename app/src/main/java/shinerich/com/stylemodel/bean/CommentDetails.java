package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class CommentDetails {
    private String id; //65;,
    private String content; //把 v 哈比 v 哈比 v 哈比 v 好吧 v 哈比 v 哈v 不哈 v 疤痕 v 吧不哈比 v 哈比 v 啊哈v 疤痕 v 疤痕 v 吧不 v 哈比 v 啊;,
    private String user_id; //275;,
    private String nickname; //su161101275;,
    private String usericon; //http://back.d3rich.com/uploads/avatar/100x100/2016/11/20161103180940_25518.jpg;,
    private String addtime; //1478073124;,
    private int praise_num; //2;,
    private String is_praise; //1,ise;
    private List<ReplyBean> reply;
    private int headFlag;


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

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getPraise_num() {
        return praise_num;
    }

    public void setPraise_num(int praise_num) {
        this.praise_num = praise_num;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public List<ReplyBean> getReply() {
        return reply;
    }

    public void setReply(List<ReplyBean> reply) {
        this.reply = reply;
    }

    public int getHeadFlag() {
        return headFlag;
    }

    public void setHeadFlag(int headFlag) {
        this.headFlag = headFlag;
    }
}
