package com;

import javafx.application.Application;
import javafx.stage.Stage;



import com.config.StorageConfig;
import com.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;



//@SpringBootApplication
//class ClientFileApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(ClientFileApplication.class, args);
//    }
//}


@SpringBootApplication
@EnableConfigurationProperties(StorageConfig.class)
public class ClientApplication extends Application {

    public void start(Stage stage) {
        Connection.start(stage);
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        launch();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}


