package shinerich.com.stylemodel.bean;

import java.util.List;

/**
 * 更新
 *
 * @author hunk
 */

public class UpdateBean {

    private int type;                    //0、不更新1、强制更新2、非强制更新
    private List<String> content;              //内容更新
    private String url;                  //更新地址
    private String version;
    private String size;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    @Override
    public String toString() {
        return "UpdateBean{" +
                "type=" + type +
                ", content=" + content +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
