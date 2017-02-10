package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class BloggerDetailBean {

    private String id; //271,
    private String ename; //Audrey Rpgers,
    private String avatar; //http://s.stylemode.com/images/thumb/thumb-164x164.jpg,

    private String avatar_big; //http://s.stylemode.com/images/thumb/thumb-164x164.jpg,
    private String is_sub; //0,//是否订阅  0否 1是
    private String type; //1//类型 0编辑 1 博主 2栏目


    private String nickname; // Maria Jernov;,//博主昵称
    private String usericon; // http://b.style.com/uploads/avatar/100x100/2016/08/20160808055731_70701.jpg;,//博主头像
    private String is_select;// 0,//是否选中


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_big() {
        return avatar_big;
    }

    public void setAvatar_big(String avatar_big) {
        this.avatar_big = avatar_big;
    }

    public String getIs_sub() {
        return is_sub;
    }

    public void setIs_sub(String is_sub) {
        this.is_sub = is_sub;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }
}
