package shinerich.com.stylemodel.bean.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import shinerich.com.stylemodel.bean.UserInfo;

/**
 * Created by Administrator on 2016/9/12.
 */
public class RealmHelper {

    public static final String DB_NAME = "stylemodel.db";

    private Realm mRealm;

    public RealmHelper(Context mContext) {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder(mContext).
                deleteRealmIfMigrationNeeded().name(DB_NAME).build());
    }

    public void addUser() {
        mRealm.beginTransaction();
        UserInfo info = mRealm.createObject(UserInfo.class);

    }

    public void addObject(Class<? extends RealmObject> object, int id) {
        mRealm.beginTransaction();
        RealmObject ob = mRealm.createObject(RealmObject.class);
        mRealm.commitTransaction();

    }


}
