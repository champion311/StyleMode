package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class CommentResponseBean {
    private String total_num;

    private List<CommentDetails> hotlist;


    private List<CommentDetails> newlist;


    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public List<CommentDetails> getHotlist() {
        return hotlist;
    }

    public void setHotlist(List<CommentDetails> hotlist) {
        this.hotlist = hotlist;
    }

    public List<CommentDetails> getNewlist() {
        return newlist;
    }

    public void setNewlist(List<CommentDetails> newlist) {
        this.newlist = newlist;
    }
}
