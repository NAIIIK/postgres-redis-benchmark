package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityDAO;
import com.javarush.domain.City;
import com.javarush.domain.CountryLanguage;
import com.javarush.redis.CityCountry;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Set;

public class BenchmarkService {

    private final SessionFactory sessionFactory;
    private final RedisClient  redisClient;
    private final ObjectMapper objectMapper;
    private final CityDAO cityDAO;

    public BenchmarkService(SessionFactory sessionFactory, RedisClient redisClient, ObjectMapper objectMapper) {
        this.sessionFactory = sessionFactory;
        this.redisClient = redisClient;
        this.objectMapper = objectMapper;
        this.cityDAO = new CityDAO(sessionFactory);
    }

    public void testRedisPerformance(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();

            for(Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    objectMapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Ошибка: Не удалось подключиться к Redis! Проверьте, запущен ли контейнер.", e);
                }
            }
        }
    }

    public void testPostgresPerformance(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            for (Integer id : ids) {
                City city = cityDAO.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getCountryLanguages();
            }

            session.getTransaction().commit();
        }
    }
}
