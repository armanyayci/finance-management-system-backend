package com.sau.swe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Configuration
public class DataSourceConfig {
    @Autowired
    private Environment environment;
    @Bean
    public DataSource hikariDataSource() {
        HikariConfig hikariConfig = getHikariConfig();
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "select id from TRANSACTIONS";
            ResultSet set = statement.executeQuery(sql);
            while (set.next()){
                System.out.println(set.getInt("id"));
            }
            System.out.println("health check success.");    //TODO sout will be replaced with log4j2
        } catch (SQLException e) {
            System.out.println("health check success failed"); //TODO sout will be replaced with log4j2
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    private HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("finance.mssql.db.driverClassName"));
        hikariConfig.setJdbcUrl(environment.getProperty("finance.mssql.db.jdbc"));
        hikariConfig.setUsername(environment.getProperty("finance.mssql.db.username"));
        hikariConfig.setPassword(environment.getProperty("finance.mssql.db.password"));
        hikariConfig.addDataSourceProperty("databaseName", environment.getProperty("finance.mssql.db.databaseName"));
        hikariConfig.addDataSourceProperty("trustServerCertificate",environment.getProperty("finance.mssql.db.trustSertificate"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(environment.getProperty("finance.db.hikari.maxPoolSize"))));
        hikariConfig.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(environment.getProperty("finance.db.hikari.minIdle"))));
        hikariConfig.setIdleTimeout(Integer.parseInt(Objects.requireNonNull(environment.getProperty("finance.db.hikari.idleTimeOut"))));
        hikariConfig.setConnectionTimeout(Integer.parseInt(Objects.requireNonNull(environment.getProperty("finance.db.hikari.connTimeout"))));
        hikariConfig.setMaxLifetime(Integer.parseInt(Objects.requireNonNull(environment.getProperty("finance.db.hikari.maxLifeTime"))));
        return hikariConfig;
    }
}