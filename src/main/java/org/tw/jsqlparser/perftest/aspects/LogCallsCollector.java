package org.tw.jsqlparser.perftest.aspects;

import org.aspectj.lang.JoinPoint;

/**
 *
 * @author toben
 */
public class LogCallsCollector implements JavaCCCollector {

    long count = 0;

    @Override
    public void beforeMethodCall(JoinPoint thisJoinPoint) {
        count++;
        if (count % 1000000 == 0) {
            System.out.println(count + " methods called");
        }
    }

    @Override
    public void afterMethodCall(JoinPoint thisJoinPoint) {
    }

    public long getCount() {
        return count;
    }
}
