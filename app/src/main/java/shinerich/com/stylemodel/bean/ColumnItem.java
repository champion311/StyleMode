package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ColumnItem implements Serializable {

    private int id;//1推荐 2时装 3生活家4美妆 5视频 6嗨国 7博主

    private String name;

    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
