package shinerich.com.stylemodel.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.robolectric.RuntimeEnvironment;

import shinerich.com.stylemodel.ContextHolder;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ContextRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        ContextHolder.setContext(RuntimeEnvironment.application);
        String className = description.getClassName();
        String methodName = description.getMethodName();
        System.out.print(className + " " + methodName);
        return base;
    }
}
