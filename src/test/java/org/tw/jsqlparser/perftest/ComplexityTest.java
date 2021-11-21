package org.tw.jsqlparser.perftest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.io.IOUtils;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tw.jsqlparser.perftest.aspects.JavaCCMethodsAspect;
import org.tw.jsqlparser.perftest.aspects.LogCallsCollector;
import org.tw.jsqlparser.perftest.aspects.LogCollector;
import org.tw.jsqlparser.perftest.aspects.LookaheadCounter;
import org.tw.jsqlparser.perftest.aspects.MethodCounter;

/**
 *
 * @author toben
 */
public class ComplexityTest {

    @Test
    @Disabled
    public void testComplexity1() throws JSQLParserException, SQLException {
        MethodCounter counter = new MethodCounter();
        LogCallsCollector calls = new LogCallsCollector();
        LookaheadCounter lookahead = new LookaheadCounter();
        JavaCCMethodsAspect.init(new LogCollector(), counter, calls, lookahead);
        Statement stmt = CCJSqlParserUtil.parse("select * from mytable");
        System.out.println("counter=" + calls.getCount());
        System.out.println("---- global method counts ----");
        counter.logMethodCounts();
        System.out.println("---- log lookahead counts ----");
        lookahead.logMethodCalls();
    }

    @Test
    @Disabled
    public void testComplexity2() throws JSQLParserException, IOException, SQLException {
        MethodCounter counter = new MethodCounter();
        LogCallsCollector calls = new LogCallsCollector();
        LookaheadCounter lookahead = new LookaheadCounter();
        //JavaCCMethodsAspect.init(counter, calls, lookahead);
        JavaCCMethodsAspect.init();
        String sql = IOUtils.toString(ComplexityTest.class.getResourceAsStream("large-sql-issue-235.txt"));
        Statement stmt = CCJSqlParserUtil.parse(sql);
        System.out.println("counter=" + calls.getCount());
        System.out.println("---- global method counts ----");
        counter.logMethodCounts();
        System.out.println("---- log lookahead counts ----");
        lookahead.logMethodCalls();
    }

    @Test
    public void testComplexityOutput1() throws IOException {
        boolean result = true;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ComplexityTest.class.getResourceAsStream("output1.sql")));) {
            String sql;
            while ((sql = reader.readLine()) != null) {
                MethodCounter counter = new MethodCounter();
                LogCallsCollector calls = new LogCallsCollector();
                LookaheadCounter lookahead = new LookaheadCounter();

                //JavaCCMethodsAspect.init(counter, calls, lookahead);
                JavaCCMethodsAspect.init();

                System.out.println("testing sql=" + sql);
                try {
                    Statement stmt = CCJSqlParserUtil.parse(sql);
                } catch (JSQLParserException ex) {
                    Logger.getLogger(ComplexityTest.class.getName()).log(Level.SEVERE, null, ex);
                    result = false;
                }
                System.out.println("counter=" + calls.getCount());
                System.out.println("---- global method counts ----");
                counter.logMethodCounts();
                System.out.println("---- log lookahead counts ----");
                lookahead.logMethodCalls();
            }
        }
        if (!result) {
            fail("at least one sql failed to be parsed");
        }
    }

    @Test
    @Disabled
    public void testComplexityOutput2() throws JSQLParserException, IOException, SQLException {
        boolean result = true;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ComplexityTest.class.getResourceAsStream("output2.sql")));) {
            String sql;
            while ((sql = reader.readLine()) != null) {
                MethodCounter counter = new MethodCounter();
                LogCallsCollector calls = new LogCallsCollector();
                LookaheadCounter lookahead = new LookaheadCounter();

                //JavaCCMethodsAspect.init(counter, calls, lookahead);
                JavaCCMethodsAspect.init();

                System.out.println("testing sql=" + sql);
                try {
                    Statement stmt = CCJSqlParserUtil.parse(sql);
                } catch (JSQLParserException ex) {
                    Logger.getLogger(ComplexityTest.class.getName()).log(Level.SEVERE, null, ex);
                    result = false;
                }
                System.out.println("counter=" + calls.getCount());
                System.out.println("---- global method counts ----");
                counter.logMethodCounts();
                System.out.println("---- log lookahead counts ----");
                lookahead.logMethodCalls();
            }
        }
        if (!result) {
            fail("at least one sql failed to be parsed");
        }
    }
    
    @Test
    @Disabled
    public void testComplexitySimpleCaseWhen() throws JSQLParserException, IOException, SQLException {
        MethodCounter counter = new MethodCounter();
        LogCallsCollector calls = new LogCallsCollector();
        LookaheadCounter lookahead = new LookaheadCounter();
        JavaCCMethodsAspect.init(counter, calls, lookahead);
        String sql = "SELECT CASE WHEN ( CASE WHEN ( CASE WHEN ( 1 ) THEN 0 END ) THEN 0 END ) THEN 0 END FROM a";
        Statement stmt = CCJSqlParserUtil.parse(sql);
        System.out.println("counter=" + calls.getCount());
        System.out.println("---- global method counts ----");
        counter.logMethodCounts();
        System.out.println("---- log lookahead counts ----");
        lookahead.logMethodCalls();
    }
}
