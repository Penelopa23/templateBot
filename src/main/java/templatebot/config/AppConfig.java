package templatebot.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import templatebot.handlers.MessageHandler;
import templatebot.handlers.PayHandler;

@Configuration
@AllArgsConstructor
public class AppConfig {

    private final TelegramConfig botConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    @Bean
    public TelegramBotConfig springWebhookBot(SetWebhook setWebhook, MessageHandler messageHandler, PayHandler payHandler) {
        TelegramBotConfig bot = new TelegramBotConfig(setWebhook, messageHandler, payHandler);
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getUserName());
        bot.setBotPath(botConfig.getWebHookPath());
        bot.setPayToken(botConfig.getPayToken());

        return bot;
    }
}