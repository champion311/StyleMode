package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 * 相关搜索提示
 *
 * @author hunk
 */
public class DiscoveryWords implements Serializable {


    private String value;  //关键词

    public DiscoveryWords() {

    }

    public DiscoveryWords(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DiscoveryWords{" +
                "value='" + value + '\'' +
                '}';
    }
}
