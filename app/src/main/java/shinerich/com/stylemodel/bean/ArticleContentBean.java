package shinerich.com.stylemodel.bean;

/**
 * Created by Administrator on 2016/10/31.
 */
public class ArticleContentBean {
    private String id; // 9091;,//文章id
    private String type; // 0;,//文章类型 0文章2视频3博文
    private String title; // ARTĒ 2016全新Indigenous系列：为夏日增添民族色彩;,//分享标题
    private String desc; // 绚丽而独特的饰物，一向是夏日的潮流元素。 ARTĒ推出Indigenous系列，将澳洲原著民艺术结合时尚，以璀璨闪亮的金光晶钻，为夏日注入活力，彰显女士的独特风采。;,//分享描述

    private String thumb; // http://back.d3rich.com/uploads/video/120x120/2016/08/20160802045712_23442.jpg;,
    private String url; // http://front.d3rich.com/app/content/detail/id/9091/type/0/uid/269/key/4b98910a8fb805e7d8f086982ee7d3e9;,//h5的url
    private String share_url; // http://m.stylemode.com/fashion/luxury/9091.html;,//分享的url
    private String video_url; //null,//视频url
    private String is_collect;// 0,//是否收藏 0未收藏 1已收藏
    private String comment_num; // 23;//评论数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }
}
