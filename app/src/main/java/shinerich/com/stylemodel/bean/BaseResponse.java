package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/9/14.
 */
public class BaseResponse<T> {

    int code;                 //0:数据正确交换   200:被踢 和未登录

    String msg;

    T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        return code == 0;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
