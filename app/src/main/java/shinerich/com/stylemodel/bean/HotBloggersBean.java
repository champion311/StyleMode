package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/10/21.
 */
public class HotBloggersBean {
    private String id; //"195",//id
    private String username; //"liuwenjuan",
    private String nickname; //"刘雯娟",//昵称
    private String usericon; //"http://b.style.com/uploads/avatar/130x130/2015/06/20150605171631_84772.jpg",//头像
    private int is_select;// 0//是否选中  0不选中  1选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getIs_select() {
        return is_select;
    }

    public void setIs_select(int is_select) {
        this.is_select = is_select;
    }
}
