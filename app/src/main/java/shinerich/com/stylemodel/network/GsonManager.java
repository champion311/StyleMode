package shinerich.com.stylemodel.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Administrator on 2016/8/26.
 */
public class GsonManager {

    public static Gson getGson(){
        Gson gson=new GsonBuilder().
                serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").
                create();
        return gson;
    }
}
