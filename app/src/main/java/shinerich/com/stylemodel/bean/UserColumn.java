package shinerich.com.stylemodel.bean;

import android.provider.SyncStateContract;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import shinerich.com.stylemodel.constant.AppConstants;
import shinerich.com.stylemodel.network.CacheInterceptor;
import shinerich.com.stylemodel.network.GsonManager;

/**
 * Created by Administrator on 2016/9/27.
 */
public class UserColumn extends RealmObject implements Serializable {

    public UserColumn() {

    }

    public UserColumn(int id) {
        this.id = id;
    }

    @PrimaryKey
    private int id; //为0的时候用户没有登录

    @Ignore
    private List<ColumnItem> columnItems;

    @NonNull
    public String getArraysGsonStr() {
        return arraysGsonStr;
    }

    public void setArraysGsonStr(@NonNull String arraysGsonStr) {
        this.arraysGsonStr = arraysGsonStr;
    }

    @NonNull
    private String arraysGsonStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public List<ColumnItem> getColumnItems() {
        return columnItems;
    }

    public void setColumnItems(List<ColumnItem> columnItems) {
        this.columnItems = columnItems;
    }

    public void setGsonStr() {
        arraysGsonStr = GsonManager.getGson().toJson(columnItems);
    }

    public void setGsonStr(List<ColumnItem> columnItems) {
        arraysGsonStr = GsonManager.getGson().toJson(columnItems);
        this.columnItems = columnItems;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ColumnItem item : columnItems) {
            sb.append(item.getId() + ",");
        }
        return sb.toString();
    }
}
