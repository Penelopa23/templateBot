package templatebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@EnableScheduling
public class TemplateBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateBotApplication.class);
    }
}
