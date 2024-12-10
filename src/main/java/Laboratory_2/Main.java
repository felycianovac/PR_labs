package Laboratory_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackages = "Laboratory_3")
@ComponentScan(basePackages = "Laboratory_2")


public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    }
