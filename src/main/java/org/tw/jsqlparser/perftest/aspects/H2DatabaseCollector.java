package org.tw.jsqlparser.perftest.aspects;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.aspectj.lang.JoinPoint;

/**
 * Collect method calls into a H2 database.
 * @author toben
 */
public final class H2DatabaseCollector implements JavaCCCollector {

    private final Connection con;
    private final QueryRunner runner = new QueryRunner();
    private final String name;

    public H2DatabaseCollector(String name, File database) throws SQLException {
        this.name = name;
        con = DriverManager.getConnection("jdbc:h2:file:./target/statsdata/data");
        runner.update(con, "create table data_" + name + " (id identity, signature varchar(255))");
    }

    public void closeDatabase() {
        DbUtils.closeQuietly(con);
    }

    @Override
    public void beforeMethodCall(JoinPoint thisJoinPoint) {
        String key = thisJoinPoint.toLongString();
        try {
            runner.update(con, "insert into data_" + name + " (signature) values (?)", key);
        } catch (SQLException ex) {
            Logger.getLogger(JavaCCMethodsAspect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void afterMethodCall(JoinPoint thisJoinPoint) {
        
    }
    
    private Map<String, AtomicInteger> computeMethodCount() {
        Map<String, AtomicInteger> methodCount = new HashMap<>();

        try (Statement stmt = con.createStatement(); ResultSet res = stmt.executeQuery("select signature from data_" + name)) {
            while (res.next()) {
                String key = res.getString(1);
                AtomicInteger count = methodCount.get(key);

            if (count == null) {
                count = new AtomicInteger();
                methodCount.put(key, count);
            }

            count.incrementAndGet();
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCCMethodsAspect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return methodCount;
    }


    public void logMethodCounts() {
        Map<String, AtomicInteger> methodCount = computeMethodCount();
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
