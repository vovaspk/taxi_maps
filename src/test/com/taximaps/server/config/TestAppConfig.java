package com.taximaps.server.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan({"com.taximaps.server"})
@PropertySource({"classpath:/application.properties"})
@EnableJpaRepositories("com.taximaps.server.repository")
public class TestAppConfig extends WebMvcConfigurationSupport {

    @Bean
    DataSource dataSource( @Value("${spring.datasource.url}") String databaseUrl,
                             @Value("${spring.datasource.username}") String datasourceUsername,
                             @Value("${spring.datasource.password}") String datasourcePassword){



         return DataSourceBuilder
                 .create()
                 .username(datasourceUsername)
                 .password(datasourcePassword)
                 .url(databaseUrl)
                 .build();
    }

}
