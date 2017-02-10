package shinerich.com.stylemodel.bean.db;

/**
 * Created by Administrator on// 2//016///10///18.
 */
public class CateDataBean {

    private String cate_data; //栏目内容
    private String id; // 11;,//栏目id
    private String name; // 珠宝腕表;,//栏目名称
    private String is_sub;// 0//是否已订阅

    private String type;//;2 //栏目所属类型 用户订阅操作识别

    private String avatar_big;  //http://s.stylemode.com/images/thumb/thumb//-1//64//x1//64.jpg;,

    private String avatar; //http://s.stylemode.com/images/thumb/thumb//-1//64//x1//64.jpg;,

    public String getCate_data() {
        return cate_data;
    }

    public void setCate_data(String cate_data) {
        this.cate_data = cate_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAvatar_big() {
        return avatar_big;
    }

    public void setAvatar_big(String avatar_big) {
        this.avatar_big = avatar_big;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
