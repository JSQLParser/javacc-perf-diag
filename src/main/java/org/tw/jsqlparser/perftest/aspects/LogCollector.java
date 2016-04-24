package org.tw.jsqlparser.perftest.aspects;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

/**
 *
 * @author toben
 */
public class LogCollector implements JavaCCCollector {
    int indent = 0;

    @Override
    public void beforeMethodCall(JoinPoint thisJoinPoint) {
        System.out.println(StringUtils.repeat("  ", indent) + "called " + thisJoinPoint.getSignature().getName());
        indent++;
    }

    @Override
    public void afterMethodCall(JoinPoint thisJoinPoint) {
        System.out.println(StringUtils.repeat("  ", indent) + "returned from " + thisJoinPoint.getSignature().getName());
        indent--;
    }
}
