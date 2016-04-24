package org.tw.jsqlparser.perftest.aspects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.aspectj.lang.JoinPoint;

/**
 *
 * @author toben
 */
public class MethodCounter implements JavaCCCollector {

    private final Map<String, AtomicInteger> methodCount = new HashMap<>();

    @Override
    public void beforeMethodCall(JoinPoint thisJoinPoint) {
        String key = thisJoinPoint.toLongString();
        AtomicInteger count = methodCount.get(key);

        if (count == null) {
            count = new AtomicInteger();
            methodCount.put(key, count);
        }

        count.incrementAndGet();
    }

    @Override
    public void afterMethodCall(JoinPoint thisJoinPoint) {
        
    }
    
    public Map<String, AtomicInteger> getMethodCount() {
        return methodCount;
    }

    public void logMethodCounts() {
        List<Map.Entry<String, AtomicInteger>> list = new ArrayList<>(methodCount.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, AtomicInteger>>() {
            @Override
            public int compare(Map.Entry<String, AtomicInteger> o1, Map.Entry<String, AtomicInteger> o2) {
                return Integer.compare(o1.getValue().intValue(), o2.getValue().intValue());
            }
        });

        for (Map.Entry<String, AtomicInteger> item : list) {
            System.out.println(item.getValue() + " - " + item.getKey());
        }
    }
}
