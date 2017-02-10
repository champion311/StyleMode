package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 我的回复
 *
 * @author hunk
 */
public class MyReply implements Serializable {

    private  MessageReply reply;         //回复信息
    private  MessageComment comment;     //评论信息
    private  MessageArticle article;     //文章信息


    public MessageReply getReply() {
        return reply;
    }

    public void setReply(MessageReply reply) {
        this.reply = reply;
    }

    public MessageComment getComment() {
        return comment;
    }

    public void setComment(MessageComment comment) {
        this.comment = comment;
    }

    public MessageArticle getArticle() {
        return article;
    }

    public void setArticle(MessageArticle article) {
        this.article = article;
    }

}
