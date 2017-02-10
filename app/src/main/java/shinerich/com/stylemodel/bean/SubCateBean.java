package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/10/21.
 */
public class SubCateBean {
    //

    private String id; //5,//id
    private String username; //theone,
    private String nickname; //Mode One,//名称
    private String type; //2,//类型  2是栏目  0 是编辑
    private String usericon; //,//图标
    private String is_select; //0//是否选中 0不选中  1选中

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
