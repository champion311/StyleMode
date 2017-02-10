package shinerich.com.stylemodel.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 发现首页
 *
 * @author hunk
 */
public class DiscoveryHome implements Serializable {

    private int page;
    private List<DiscoveryItem> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<DiscoveryItem> getList() {
        return list;
    }

    public void setList(List<DiscoveryItem> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "DiscoveryHome{" +
                "page=" + page +
                ", list=" + list +
                '}';
    }
}
