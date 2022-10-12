package templatebot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import templatebot.handlers.MessageHandler;
import templatebot.handlers.PayHandler;
import templatebot.utils.UpdateRouter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBotConfig extends SpringWebhookBot {

    String botPath;
    String botUsername;
    String botToken;


    @Autowired
    UpdateRouter updateRouter;

    public TelegramBotConfig(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return handleUpdate(update);
    }

    public BotApiMethod<?> handleUpdate(Update update) {
       return updateRouter.route(update);
    }



}