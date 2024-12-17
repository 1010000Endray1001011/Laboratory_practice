package com.kitisgang.lpz66466.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        //запросы с любых источников (в продакшене следует ограничить)
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        //основные HTTP методы
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        //все заголовки
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        //credentials (например, cookies)
        config.setAllowCredentials(true);

        //время кэширования предварительных запросов (preflight requests)
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
    }

    // Дополнительные настройки для работы с базой данных
    @Bean
    public org.springframework.jdbc.datasource.DataSourceTransactionManager transactionManager(
            javax.sql.DataSource dataSource) {
        return new org.springframework.jdbc.datasource.DataSourceTransactionManager(dataSource);
    }
}
