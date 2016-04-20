package com.example;

import com.example.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.cache.Cache;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {

    private static final int TEST_DATA_COUNT = 100000;
    private static final int BENCHMARK_INTERVAL = 1;
    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("igniting");

//        System.setProperty("IGNITE_H2_DEBUG_CONSOLE", "true");
        Map<Integer, Trade> testData = TestUtil.generateTestData(TEST_DATA_COUNT);
        Map<Integer, SecurityDetail> securityDetailMap = TestUtil.generateTestData(testData);
        ApplicationContext context = new ClassPathXmlApplicationContext("/application-context.xml");
        Ignite ignite = context.getBean(Ignite.class);

        IgniteCache<Integer, Trade> indexedTradeCache = ignite.getOrCreateCache(context.getBean("indexedTradeCacheConfig", CacheConfiguration.class));
        IgniteCache<Integer, Trade> tradeCache = ignite.getOrCreateCache(context.getBean("tradeCacheConfig", CacheConfiguration.class));
        IgniteCache<Integer, SecurityDetail> securityDetailCache = ignite.getOrCreateCache(context.getBean("indexedSecurityDetailCacheConfig", CacheConfiguration.class));

        LOGGER.info("start populating non indexed collection");
        populateCache(testData, tradeCache);

        LOGGER.info("start populating indexed collection");
        populateCache(testData, indexedTradeCache);

        LOGGER.info("start populating Security detail cache");
        populateCache(securityDetailMap, securityDetailCache);

        LOGGER.info("benchmarking equals for non-indexed");
        benchMarkEquals(testData, tradeCache);

        LOGGER.info("benchmarking equals for indexed");
        benchMarkEquals(testData, indexedTradeCache);

        LOGGER.info("benchmarking equals for non-indexed on two fields");
        benchMarkEqualsOnGroupIndex(tradeCache);

        LOGGER.info("benchmarking equals for indexed on two fields (both indexed)");
        benchMarkEqualsOnGroupIndex(indexedTradeCache);

        LOGGER.info("cross cache join - finding out sum of all buy and sell trades for all sectors");
        queryCrossCache(indexedTradeCache, securityDetailCache);

        ((AbstractApplicationContext) context).close();

        LOGGER.info("end");
    }

    private static void queryCrossCache(IgniteCache<Integer, Trade> indexedTradeCache, IgniteCache<Integer, SecurityDetail> securityDetailCache) {
        long startTime = System.currentTimeMillis();
        String sql = "SELECT s.SECTOR, t.SIDE, sum(t.amount)  FROM \"securityDetailCache\".SECURITYDETAIL s, \"indexedTradeCache\".TRADE t where s.sedol=t.sedol group by s.sector, t.side order by 1,2";
        List<List<?>> result = indexedTradeCache.query(new SqlFieldsQuery(sql)).getAll();
        for(List<?> row: result){
            LOGGER.info(row.toString());
        }
        long totalTime = System.currentTimeMillis() - startTime;
        LOGGER.info(String.format("total time taken to run cross cache with 2 level of grouping:%d ms",  totalTime ));
    }

    private static <V> void populateCache(Map<Integer, V> testData, IgniteCache<Integer, V> cache) {
        long startTime = System.currentTimeMillis();
        cache.putAll(testData);
        long totalTime = System.currentTimeMillis() - startTime;
        LOGGER.info(String.format("collection populated with: %d recs in: %d ms", cache.size(), totalTime));
    }

    private static void benchMarkEquals(Map<Integer, Trade> testData, IgniteCache<Integer, Trade> cache) {
        int count = 0;
        Random random = new Random();
        String sql = "sedol = ?";
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(BENCHMARK_INTERVAL);
        while (System.currentTimeMillis() < endTime) {
            String sedol = testData.get(random.nextInt(TEST_DATA_COUNT)).getSedol();
            List<Cache.Entry<Integer, Trade>> result = cache.query(new SqlQuery<Integer, Trade>(Trade.class, sql).setArgs(sedol)).getAll();
//            LOGGER.info(String.format("for sedol: %s matches found: %d", sedol, result.size()));
            count++;
        }
        LOGGER.info(String.format("performed: %d searches in: %d secs ", count, BENCHMARK_INTERVAL));
    }

    private static void benchMarkEqualsOnGroupIndex(IgniteCache<Integer, Trade> cache) {
        int count = 0;
        Random random = new Random();
        String sql = "side = ? AND strategy = ?";
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(BENCHMARK_INTERVAL);
        while (System.currentTimeMillis() < endTime) {
            Side side = Side.values()[random.nextInt(Side.values().length)];
            Strategy strategy = Strategy.values()[random.nextInt(Strategy.values().length)];
            List<Cache.Entry<Integer, Trade>> result = cache.query(new SqlQuery<Integer, Trade>(Trade.class, sql).setArgs(side, strategy)).getAll();
//            LOGGER.info(String.format("for side: %s and strategy: %s matches found: %d", side, strategy, result.size()));
            count++;
        }
        LOGGER.info(String.format("performed: %d searches in: %d secs ", count, BENCHMARK_INTERVAL));
    }


}
