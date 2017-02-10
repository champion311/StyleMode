package shinerich.com.stylemodel.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 热门关键词
 *
 * @author hunk
 */
public class DiscoveryHotTitle implements Serializable {

    private List<String> value;


    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DiscoveryHotTitle{" +
                "value=" + value +
                '}';
    }
}
