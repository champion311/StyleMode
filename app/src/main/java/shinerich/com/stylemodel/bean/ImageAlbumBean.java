package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ImageAlbumBean {

    private String id; //5;,//内容id
    private String type;//: "1",//内容类型  1图集 "title; "摩登主义美学趣致：JIL SANDER 2017早春度假系列;,
    private String desc; //JIL SANDER 2017 早春度假系列延续传统经典，注入摩登主义美学趣致，并巧妙融入一丝新鲜尝试，完美打造假日衣橱最佳搭配。本季面料大量运用天然纤维，棉，丝绸，皮革，相互交织，巧妙组合。;,

    private String thumb; //http://back.d3rich.com/uploads/video/120x120/2016/08/20160802045712_23442.jpg;,
    private String share_url; //http://m.stylemode.com/fashion/fashionideas/pic9089.html;,
    private String comment_num; //0;,//评论数
    private String is_collect;// 0,//是否收藏 0未收藏1已收藏
    private String title;
    private List<ImageListBean> list;

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

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public List<ImageListBean> getList() {
        return list;
    }

    public void setList(List<ImageListBean> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
