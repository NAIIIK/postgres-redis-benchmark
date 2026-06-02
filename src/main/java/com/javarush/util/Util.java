package com.javarush.util;

public class Util {
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";


    public static final String DB_HOST = "localhost";
    public static final Integer DB_PORT = 5432;
    public static final Integer REDIS_PORT = 6379;

    public static final String CHANGE_LOG_PATH = "db/changelog/master.yaml";

    public static final String DEFAULT_SCHEMA_NAME = "world";

    public static final String DB_URL = "jdbc:postgresql://"
            + DB_HOST + ":" + DB_PORT + "/world";

    public static final String P6SPY_URL = "jdbc:p6spy:postgresql://"
            + DB_HOST + ":" + DB_PORT + "/world";

    public static final String DB_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";

    public static final String P6SPY_DRIVER = "com.p6spy.engine.spy.P6SpyDriver";
    public static final String DB_DRIVER = "org.postgresql.Driver";
}
