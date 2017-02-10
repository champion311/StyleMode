package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/11/7.
 */
public class Danmu {

    public int id; //55,
    public String nickname; //333,

    public int user_id;//276,
    public String usericon; //http://s.stylemode.com/uploads/article/100x100/2016/10/20161028105008_28237.jpg,
    public String content; //去问去问去问
    public boolean isFromLocal = false;

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
