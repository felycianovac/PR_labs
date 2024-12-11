package Config;

import FTP.FTPFetcher;
import org.springframework.context.annotation.Bean;

public class AppConfig {
    @Bean
    public FTPFetcher ftpFetcher() {
        return new FTPFetcher();
    }
}
