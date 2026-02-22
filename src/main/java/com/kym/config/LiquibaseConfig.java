package com.kym.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        springLiquibase.setShouldRun(true);

        System.out.println("========================================");
        System.out.println(">>> Liquibase Configuration Created <<<");
        System.out.println(">>> Change Log: " + springLiquibase.getChangeLog());
        System.out.println("========================================");

        return springLiquibase;
    }
}
