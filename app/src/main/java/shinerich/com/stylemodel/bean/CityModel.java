package shinerich.com.stylemodel.bean;

import java.io.Serializable;

/**
 *   城市名字
 * @author hunk
 */
public class CityModel  implements Serializable {

    private String id;
    private String name;

    public CityModel(){

    }

    public CityModel(String id,String name){
        this.id=id;
        this.name=name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
