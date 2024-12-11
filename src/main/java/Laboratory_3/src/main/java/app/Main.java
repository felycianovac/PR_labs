package app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import FTP.FTPFetcher;

@SpringBootApplication
@ComponentScan(basePackages = {"FTP", "rabbitMQ", "RaftSimulation", "Config"})
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        FTPFetcher ftpFetcher = context.getBean(FTPFetcher.class);
        ftpFetcher.startFetching();
    }
}