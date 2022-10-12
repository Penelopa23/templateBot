package templatebot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import templatebot.handlers.MessageHandler;
import templatebot.handlers.PayHandler;

@Component
public class UpdateRouter {
    @Autowired
    MessageHandler messageHandler;
    @Autowired
    PayHandler payHandler;
    @Value("${telegrambot.payToken}")
    String payToken;

    public BotApiMethod<?> route(Update update) {
        if(update.hasMessage()) { //Ответы на запросы содержащие сообщения
            if (!update.getMessage().hasViaBot() && !update.getMessage().hasSuccessfulPayment()) {
                return messageHandler.startMessage(update);
            } else if (update.getMessage().hasViaBot()) {
                return payHandler.invoiceForBurger(update, payToken);
            } else if (update.getMessage().hasSuccessfulPayment()) {
                return payHandler.successMesage(update);
            } else {
                try {
                    return somethingWrong(update);
                } catch (Exception e) {
                    return null;
                }
            }
            //Ответы на запросы без сообщений
        } else if (update.hasPreCheckoutQuery()) {
            return payHandler.answerPreCheckoutQuery(update);
        } else if (update.hasShippingQuery()) {
            return payHandler.shippingQuery(update);
        } else {
            try {
                return somethingWrong(update);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private SendMessage somethingWrong(Update update) {
        return new SendMessage(update.getMessage().getChatId().toString(), "It's something else, please " +
                "contact to administrator");
    }
}
