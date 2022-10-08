package templatebot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import templatebot.handlers.MessageHandler;
import templatebot.handlers.PayHandler;

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
    String payToken;

    MessageHandler messageHandler;
    PayHandler payHandler;

    public static Map<Long, Integer> settings = new HashMap<>();
    public static Map<Long, Integer> count = new HashMap<>();

    public TelegramBotConfig(SetWebhook setWebhook, MessageHandler messageHandler, PayHandler payHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.payHandler = payHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return handleUpdate(update);
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasPreCheckoutQuery()) {
            return payHandler.answerPreCheckoutQuery(update);
        }
        if(update.getShippingQuery() != null) {
            return payHandler.shippingQuery(update);
        }
        if(update.getMessage().hasSuccessfulPayment()) {
            return payHandler.successMesage(update);
        }
        if (!update.getMessage().hasViaBot()) {
            return messageHandler.startMessage(update);
        } else if (update.getMessage().hasViaBot()) {
            return payHandler.invoiceForBurger(update, payToken);
        } else {
            try {
                return new SendMessage(update.getMessage().getChatId().toString(), "It's something else, please " +
                        "contact to administrator");
            } catch (Exception e) {
                return null;
            }
        }
    }
}