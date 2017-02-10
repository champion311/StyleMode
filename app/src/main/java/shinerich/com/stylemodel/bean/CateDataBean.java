package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CateDataBean {

    //栏目内容
    private String id; //11,//栏目id
    private String name; //珠宝腕表,//栏目名称
    private String ename; //luxury,
    private String redirect; //null,
    private String status; //2,
    private String sort; //99,
    private String pid; //1,
    private String linkspos; //0,
    private String is_sub; //0//是否已订阅

    private String type;//2 //栏目所属类型 用户订阅操作识别

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

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLinkspos() {
        return linkspos;
    }

    public void setLinkspos(String linkspos) {
        this.linkspos = linkspos;
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
