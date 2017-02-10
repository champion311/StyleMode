package shinerich.com.stylemodel.bean;

/**
 * 账号设置
 *
 * @author hunk
 */

public class AccountSetting {
    private String mobile;
    private BindState sina;
    private BindState wx;
    private BindState qq;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BindState getSina() {
        return sina;
    }

    public void setSina(BindState sina) {
        this.sina = sina;
    }

    public BindState getWx() {
        return wx;
    }

    public void setWx(BindState wx) {
        this.wx = wx;
    }

    public BindState getQq() {
        return qq;
    }

    public void setQq(BindState qq) {
        this.qq = qq;
    }

    @Override
    public String toString() {
        return "AccountSetting{" +
                "mobile='" + mobile + '\'' +
                ", sina=" + sina +
                ", wx=" + wx +
                ", qq=" + qq +
                '}';
    }
}
