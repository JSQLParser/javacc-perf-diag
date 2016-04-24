package org.tw.jsqlparser.perftest.aspects;

import org.aspectj.lang.JoinPoint;

/**
 * Datacollector.
 *
 * @author toben
 */
public interface JavaCCCollector {

    void beforeMethodCall(JoinPoint thisJoinPoint);
    
    void afterMethodCall(JoinPoint thisJoinPoint);
}
