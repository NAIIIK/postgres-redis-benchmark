import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.config.AppConfig;
import com.javarush.domain.City;
import com.javarush.liquibase.LiquibaseInitializer;
import com.javarush.redis.CityCountry;

import com.javarush.service.BenchmarkService;
import com.javarush.service.DataTransferService;
import io.lettuce.core.RedisClient;
import org.hibernate.SessionFactory;

import java.util.List;

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

            for (int i = 0; i < 2; i++) {
                long startRedis = System.currentTimeMillis();
                benchmarkService.testRedisPerformance(ids);
                long stopRedis = System.currentTimeMillis();

                long startPostgres = System.currentTimeMillis();
                benchmarkService.testPostgresPerformance(ids);
                long stopPostgres = System.currentTimeMillis();

                System.out.printf("%s:\t%d ms\n", "Redis",  (stopRedis - startRedis));
                System.out.printf("%s:\t%d ms\n", "PostgreSQL",  (stopPostgres - startPostgres));

                Thread.sleep(5_000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (sessionFactory != null) sessionFactory.close();
            redisClient.shutdown();
        }
    }
}
