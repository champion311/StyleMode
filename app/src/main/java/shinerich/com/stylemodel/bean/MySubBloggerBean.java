package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class MySubBloggerBean {

    private String id; //195,//id
    private String username; //liuwenjuan,
    private String nickname; //刘雯娟,//昵称
    private String usericon; ///2015/06/20150605171631_84772.jpg,//头像
    private String addtime; //1476696537,
    private String is_blogger; //1,
    private String utype; //1 //1博主

    private boolean isSelected = true;


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

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_blogger() {
        return is_blogger;
    }

    public void setIs_blogger(String is_blogger) {
        this.is_blogger = is_blogger;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }
}
