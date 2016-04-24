package org.tw.jsqlparser.perftest.aspects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.aspectj.lang.JoinPoint;

/**
 * Counts lookaheads of each Method. Lookaheads are detected by JavaCCs method
 * names.
 *
 * @author toben
 */
public class LookaheadCounter implements JavaCCCollector {

    private final List<MethodData> lookaheadCount = new ArrayList<>();
    private final Pattern lookaheadSignature = Pattern.compile("jj_\\dR?_\\d+");

    @Override
    public void beforeMethodCall(JoinPoint thisJoinPoint) {
        Matcher matcher = lookaheadSignature.matcher(thisJoinPoint.getSignature().getName());
        if (matcher.matches()) {
            if (lookaheadCount.isEmpty()) {
                System.out.println("problematic method data");
            } else {
                lookaheadCount.get(lookaheadCount.size() - 1).lookaheadCalled();
            }
        } else if ("jj_scan_token".equals(thisJoinPoint.getSignature().getName())) {
            if (lookaheadCount.isEmpty()) {
                System.out.println("problematic method data");
            } else {
                lookaheadCount.get(lookaheadCount.size() - 1).scanTokenCalled();
            }
        } else if ("jj_ntk_f".equals(thisJoinPoint.getSignature().getName()) 
                || "jj_save".equals(thisJoinPoint.getSignature().getName())
                || "getToken".equals(thisJoinPoint.getSignature().getName())
                || "jj_add_error_token".equals(thisJoinPoint.getSignature().getName())
                || "jj_consume_token".equals(thisJoinPoint.getSignature().getName())) {
            //Skip it
        } else {
            lookaheadCount.add(new MethodData(thisJoinPoint.toLongString()));
            System.out.println(lookaheadCount.size() + " " + thisJoinPoint.toLongString());
        }
    }

    public void logMethodCalls() {
        for (MethodData data : lookaheadCount) {
            System.out.println("lookaheads=" + data.lookaheadCount + " scan_token=" + data.getScanTokenCalled() + " within " + data.getMethodSignature());
        }
    }

    @Override
    public void afterMethodCall(JoinPoint thisJoinPoint) {

    }

    public static class MethodData {

        private final String methodName;
        private int lookaheadCount = 0;
        private int scanTokenCount = 0;

        public MethodData(String methodName) {
            this.methodName = methodName;
        }

        public void lookaheadCalled() {
            lookaheadCount++;
        }

        public void scanTokenCalled() {
            scanTokenCount++;
        }

        public int getLookaheadCount() {
            return lookaheadCount;
        }

        public int getScanTokenCalled() {
            return scanTokenCount++;
        }

        public String getMethodSignature() {
            return methodName;
        }
    }
}
