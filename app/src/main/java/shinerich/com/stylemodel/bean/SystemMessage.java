package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 系统消息
 *
 * @author hunk
 */
public class SystemMessage implements Serializable {

    private String id;
    private String content;       //内容
    private String user_id;       //用户id
    private String username;
    private String outer_id;
    private String outer_type;
    private String admin_id;
    private String status="2";    //状态 1 未读 2已读
    private String addtime;  //时间


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(String outer_id) {
        this.outer_id = outer_id;
    }

    public String getOuter_type() {
        return outer_type;
    }

    public void setOuter_type(String outer_type) {
        this.outer_type = outer_type;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }


    @Override
    public String toString() {
        return "SystemMessage{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", outer_id='" + outer_id + '\'' +
                ", outer_type='" + outer_type + '\'' +
                ", admin_id='" + admin_id + '\'' +
                ", status='" + status + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
