package com.javarush.liquibase;

import com.javarush.util.Util;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseInitializer {
    public static void runMigrations() {
        String url = Util.DB_URL;
        String user = Util.DB_USER;
        String password = Util.DB_PASSWORD;
        String changeLogPath = Util.CHANGE_LOG_PATH;

        try {
            Class.forName(Util.DB_DRIVER);

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));
                database.setDefaultSchemaName(Util.DEFAULT_SCHEMA_NAME);

                System.out.println("Запуск миграций Liquibase...");

                CommandScope updateCommand = new CommandScope("update");

                updateCommand.addArgumentValue("changeLogFile", changeLogPath);
                updateCommand.addArgumentValue("database", database);
                updateCommand.addArgumentValue("resourceAccessor", new ClassLoaderResourceAccessor());
                updateCommand.addArgumentValue("defaultSchemaName", Util.DEFAULT_SCHEMA_NAME);

                updateCommand.execute();

                System.out.println("Миграции Liquibase успешно применены");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер PostgreSQL не найден.", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении миграций Liquibase.", e);
        }
    }
}
