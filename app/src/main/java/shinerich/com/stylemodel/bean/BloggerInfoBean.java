package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/9/20.
 */
public class BloggerInfoBean {
    //仅用在主页项目中


    private String id; //private String 195;,//id
    private String username; //liuwenjuan,
    private String nickname; //刘雯娟,//昵称
    private String usericon; //http://b.style.com/uploads/avatar/130x130/2015/06/20150605171631_84772.jpg,//头像
    private String is_select;
    private int type = 1;  //1//类型   1博主 2栏目

    private boolean isToAddOrRemore;


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

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }
//0//是否选中  0不选中  1选 中


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isToAddOrRemore() {
        return isToAddOrRemore;
    }

    public void setToAddOrRemore(boolean toAddOrRemore) {
        isToAddOrRemore = toAddOrRemore;
    }
}
