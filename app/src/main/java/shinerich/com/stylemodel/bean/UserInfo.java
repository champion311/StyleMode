package shinerich.com.stylemodel.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 用户信息
 *
 * @author hunk
 */
public class UserInfo extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;                 //id  272
    private String key;                //key
    private String nickname;            //昵称
    private String usericon;           //头像
    private String mobile;             //手机号
    private String addtime;           //添加时间
    private String sex = "0";           //性别  1 男 2 女
    private String birthyear;         // 出生年
    private String birthmonth;        //出生月
    private String birthday;          //出生日
    private String province;          //省份
    private String platform = "-1";     //0 新浪 1 wx 2 qq


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(String birthyear) {
        this.birthyear = birthyear;
    }

    public String getBirthmonth() {
        return birthmonth;
    }

    public void setBirthmonth(String birthmonth) {
        this.birthmonth = birthmonth;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", nickname='" + nickname + '\'' +
                ", usericon='" + usericon + '\'' +
                ", mobile='" + mobile + '\'' +
                ", addtime='" + addtime + '\'' +
                ", sex='" + sex + '\'' +
                ", birthyear='" + birthyear + '\'' +
                ", birthmonth='" + birthmonth + '\'' +
                ", birthday='" + birthday + '\'' +
                ", province='" + province + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
