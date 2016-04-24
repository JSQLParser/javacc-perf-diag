package org.tw.jsqlparser.perftest.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Aspect to record all method calls of JSqlParser and categorize it by normal
 * production or lookahead.
 *
 * @author toben
 */
@Aspect
public class JavaCCMethodsAspect {

    @Before("execution(* net.sf.jsqlparser.parser.CCJSqlParser.*(..))")
    public void beforeMethodCall(JoinPoint thisJoinPoint) {
        for (JavaCCCollector col : listener) {
            col.beforeMethodCall(thisJoinPoint);
        }
    }

    @After("execution(* net.sf.jsqlparser.parser.CCJSqlParser.*(..))")
    public void afterMethodCall(JoinPoint thisJoinPoint) {
        for (JavaCCCollector col : listener) {
            col.afterMethodCall(thisJoinPoint);
        }
    }

    private static JavaCCCollector[] listener;

    public static void init(JavaCCCollector ... listener) {
        JavaCCMethodsAspect.listener = listener;
    }
}
