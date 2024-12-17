package com.kitisgang.lpz66466;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Gas Station Network API",
                version = "1.0",
                description = "REST API для управления сетью автозаправочных станций"
        )
)
public class Lpz66466Application {
    public static void main(String[] args) {
        SpringApplication.run(Lpz66466Application.class, args);
    }
}
