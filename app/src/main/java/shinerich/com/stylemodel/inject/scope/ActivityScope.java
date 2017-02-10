package shinerich.com.stylemodel.inject.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Administrator on 2016/8/15.
 * Identifies for activity context  //  周期约activity一致
 *  //
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
