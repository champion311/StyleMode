package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/12/7.
 */
public class EditorData {

    private String id; // 31;,//编辑id
    private String ename; // Lexi;,//编辑笔名
    private String avatar; // http://s.stylemode.com/uploads/editor/130x130/2016/09/20160909070113_89537.jpg;,//编辑头像

    private String avatar_big; // http://s.stylemode.com/images/thumb/thumb-164x164.jpg;,
    private String is_sub; //0,//是否订阅 0 否 1是
    private String type; //0//类型  0编辑 1博主2栏目

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
}
