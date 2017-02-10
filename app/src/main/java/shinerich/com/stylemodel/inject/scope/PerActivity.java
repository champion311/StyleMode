package shinerich.com.stylemodel.inject.scope;

/**
 * Created by Administrator on 2016/8/26.
 */

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Administrator on 2016/8/17.
 * /一个自定义的范围注解,注解对象的生命周期应该遵循activity的生命周期
 */

@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
