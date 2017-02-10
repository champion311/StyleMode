package shinerich.com.stylemodel.inject.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2016/9/13.
 * 遵循fragment的什么周期
 */
@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
