package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ContentData {

//    private String id; //624,//id
//    private String thumbnals; //http://s.stylemode.com/uploads/blog/350x440/2016/06/20160628094341_47997.jpg,//缩略图
//    private String title; //[跟博主学穿搭]这样穿，粉色系会很高贵。,//标题
//    private int type; //3//类型  0文章、1图集、2视频、3博文

    private String id; //5,//栏目id
    private String username; //theone,
    private String nickname; //Mode One,//栏目名称
    private String addtime; //1476696563,
    private String utype; //2,
    private String usericon; //,
    private String is_select; //1,//是否选中
    private String type; //2//类型 1博主 2栏目

    private String thumbnals;

    private String title;

    private String mask;


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

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnals() {
        return thumbnals;
    }

    public void setThumbnals(String thumbnals) {
        this.thumbnals = thumbnals;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }
}
