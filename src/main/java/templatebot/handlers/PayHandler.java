package templatebot.handlers;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import templatebot.config.TelegramConfig;

import java.util.Arrays;
public class PayHandler {

    public SendInvoice invoiceForBurger(Update update, String payToken) {
        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setChatId(update.getMessage().getChatId());
        sendInvoice.setTitle(update.getMessage().getText());
        sendInvoice.setDescription("It's test burger, not edible");
        sendInvoice.setPayload("Something that user don't see, but we can use it for something");
        sendInvoice.setProviderToken(payToken);
        sendInvoice.setCurrency("rub");
        sendInvoice.setPhotoUrl("https://chefrestoran.ru/wp-content/uploads/2018/10/333977423.jpg");
        sendInvoice.setStartParameter("unique-string");
        sendInvoice.setNeedPhoneNumber(true);
        sendInvoice.setNeedEmail(true);
        sendInvoice.setPrices(Arrays.asList(new LabeledPrice(update.getMessage().getText(), 1000000)));
        return sendInvoice;
    }

    public AnswerPreCheckoutQuery answerPreCheckoutQuery(Update update) {
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
        answerPreCheckoutQuery.setOk(true);
        return answerPreCheckoutQuery;
    }

    public SendMessage successMesage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Thank you for your 'payment'! Don't worry, your imaginary credit card was not charged. " +
                "Your order is not on the way");
        return sendMessage;
    }
}
