package com.javarush.util;

import java.util.Objects;

public final class Util {

    private Util() {}

    public static final String DB_USER = checkEnvVariable("DB_USER");
    public static final String DB_PASSWORD = checkEnvVariable("DB_PASSWORD");

    public static final String DB_HOST = checkEnvVariable("DB_HOST");
    public static final int DB_PORT = parseIntEnvVariable("DB_PORT", 5432);
    public static final String REDIS_HOST = checkEnvVariable("REDIS_HOST");
    public static final int REDIS_PORT = parseIntEnvVariable("REDIS_PORT", 6379);

    public static final String DB_NAME = checkEnvVariable("DB_NAME");
    public static final String DEFAULT_SCHEMA_NAME = "world";

    public static final String DB_URL = buildDbUrl("jdbc:postgresql");

    public static final String P6SPY_URL = buildDbUrl("jdbc:p6spy:postgresql");

    public static final String DB_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";

    public static final String P6SPY_DRIVER = "com.p6spy.engine.spy.P6SpyDriver";

    public static final String CHANGE_LOG_PATH = "db/changelog/master.yaml";

    private static final int DEFAULT_CHECK_QUANTITY = 2;
    public static final int CHECK_QUANTITY = parseIntEnvVariable("CHECK_QUANTITY", DEFAULT_CHECK_QUANTITY);
    private static final String CHECK_PAUSE_ENV_KEY = "CHECK_PAUSE_SECONDS";
    private static final int DEFAULT_CHECK_PAUSE_SECONDS = 5;
    public static final int CHECK_PAUSE_MILLIS = getPauseMillis();

    private static String buildDbUrl(String dbPrefix) {
        return String.format("%s://%s:%d/%s", dbPrefix, DB_HOST, DB_PORT, DB_NAME);
    }

    private static int getPauseMillis() {
        String envValue = System.getenv(CHECK_PAUSE_ENV_KEY);

        int seconds = parseIntEnvVariable(CHECK_PAUSE_ENV_KEY, DEFAULT_CHECK_PAUSE_SECONDS);

        try {
            return Math.multiplyExact(seconds, 1_000);
        } catch (ArithmeticException e) {
            System.err.println("Обнаружено переполнение целого числа для значения " + envValue + " переменной окружения "
                    + CHECK_PAUSE_ENV_KEY + ". Используется значение по умолчанию: " + DEFAULT_CHECK_PAUSE_SECONDS);

            return Math.multiplyExact(DEFAULT_CHECK_PAUSE_SECONDS, 1_000);
        }
    }

    private static int parseIntEnvVariable(String envVariable, int defaultValue) {
        String envValue = checkEnvVariable(envVariable);

        if (envValue == null || envValue.isBlank()) return defaultValue;

        try {
            return Integer.parseInt(envValue.trim());
        } catch (NumberFormatException e) {
            System.err.println("Некорректное значение " + envValue + " для переменной окружения "
                    + envVariable + ". Используется значение по умолчанию: " + defaultValue);
            return defaultValue;
        }
    }

    private static String checkEnvVariable(String envVariable) {
        return Objects.requireNonNull(System.getenv(envVariable),
                "Переменная окружения " + envVariable + " имеет значение null.");
    }
}