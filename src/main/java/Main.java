import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.config.AppConfig;
import com.javarush.domain.City;
import com.javarush.liquibase.LiquibaseInitializer;
import com.javarush.redis.CityCountry;

import com.javarush.service.BenchmarkService;
import com.javarush.service.DataTransferService;
import com.javarush.util.Util;
import io.lettuce.core.RedisClient;
import org.hibernate.SessionFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        LiquibaseInitializer.runMigrations();

        SessionFactory sessionFactory = AppConfig.prepareRelationalDb();
        RedisClient redisClient = AppConfig.prepareRedisClient();
        ObjectMapper objectMapper = new ObjectMapper();

        DataTransferService dataTransferService = new DataTransferService(sessionFactory, redisClient, objectMapper);
        BenchmarkService benchmarkService = new BenchmarkService(sessionFactory, redisClient, objectMapper);

        try {
            List<City> allCities = dataTransferService.fetchDataFromPostgres();
            List<CityCountry> preparedData = dataTransferService.transformData(allCities);
            dataTransferService.pushToRedis(preparedData);

            List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

            Map<Integer, String> benchmarkStats = new LinkedHashMap<>();

            for (int i = 0; i < Util.CHECK_QUANTITY; i++) {
                long startRedis = System.currentTimeMillis();
                benchmarkService.testRedisPerformance(ids);
                long stopRedis = System.currentTimeMillis();

                long startPostgres = System.currentTimeMillis();
                benchmarkService.testPostgresPerformance(ids);
                long stopPostgres = System.currentTimeMillis();

                String redisPerformance = String.format("%s:\t%d ms", "Redis",  (stopRedis - startRedis));
                String postgresPerformance = String.format("%s:\t%d ms", "PostgreSQL",  (stopPostgres - startPostgres));

                System.out.println(redisPerformance);
                System.out.println(postgresPerformance);

                benchmarkStats.put(i + 1, String.format("%s; %s", redisPerformance, postgresPerformance));

                Thread.sleep(Util.CHECK_PAUSE_MILLIS);
            }

            System.out.println("--------------------------------------------");
            benchmarkStats.forEach((k, v) -> System.out.println(k + ": " + v));
            System.out.println("--------------------------------------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (sessionFactory != null) sessionFactory.close();
            redisClient.shutdown();
        }
    }
}
