package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 我的评论
 *
 * @author hunk
 */
public class MyComment implements Serializable {

    private MessageComment commentinfo;       //评论信息
    private UserInfo userinfo;                //用户信息
    private MessageArticle articleinfo;       //文章信息


    public MessageComment getCommentinfo() {
        return commentinfo;
    }

    public void setCommentinfo(MessageComment commentinfo) {
        this.commentinfo = commentinfo;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public MessageArticle getArticleinfo() {
        return articleinfo;
    }

    public void setArticleinfo(MessageArticle articleinfo) {
        this.articleinfo = articleinfo;
    }

}
