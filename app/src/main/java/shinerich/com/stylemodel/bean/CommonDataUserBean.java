package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/11/23.
 */
public class CommonDataUserBean {
    private String id; //11;,
    private String type; //2;,//类型  0编辑 1博主2栏目
    private String name; //珠宝腕表;,
    private String usericon; //http://f.style.com/app/image/fashion.png;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }
}
