package templatebot.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerShippingQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingOption;


import java.util.*;

/**
 *
 * Класс обработки платежа и доставки
 *
 */

@Slf4j
@Component
@AllArgsConstructor
public class PayHandler {

    //Отправка счета
    public SendInvoice invoiceForBurger(@NotNull Update update, String payToken) {

        //Убираем из строки "Order summary:"
        String textMessage = update.getMessage().getText().substring(16);
        //Создаём массив выбранных товаров
        String[] products = textMessage.trim().split("\n");

        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setChatId(update.getMessage().getChatId());
        sendInvoice.setTitle("Order# " + System.currentTimeMillis());
        sendInvoice.setDescription("It's test burger, not edible");
        sendInvoice.setPayload("Something that user don't see, but we can use it for something");
        sendInvoice.setProviderToken(payToken);
        sendInvoice.setCurrency("usd");
        sendInvoice.setPhotoUrl("https://chefrestoran.ru/wp-content/uploads/2018/10/333977423.jpg");
        sendInvoice.setStartParameter("unique-string");
        sendInvoice.setIsFlexible(true);
        sendInvoice.setNeedShippingAddress(true);
        sendInvoice.setNeedName(true);
        sendInvoice.setSuggestedTipAmounts(Arrays.asList(100,300,500,700));
        sendInvoice.setMaxTipAmount(80000);
        sendInvoice.setNeedPhoneNumber(true);
        sendInvoice.setNeedEmail(true);
        sendInvoice.setPrices(setProducts(products));
        return sendInvoice;
    }

    //Обработка выбора доставки
    public AnswerShippingQuery shippingQuery(Update update) {
        AnswerShippingQuery answerShippingQuery = new AnswerShippingQuery();
        answerShippingQuery.setShippingQueryId(update.getShippingQuery().getId());

        if(update.getShippingQuery().getShippingAddress().getCountryCode().equals("RU")) {
            answerShippingQuery.setErrorMessage("Sorry, we can't sent burgers in Russia");
            answerShippingQuery.setOk(false);
            return answerShippingQuery;
        }
        answerShippingQuery.setShippingOptions(Arrays.asList(fastDelivery(), regularDelivery()));
        answerShippingQuery.setOk(true);
        return answerShippingQuery;
    }

    //Предоплаты
    public AnswerPreCheckoutQuery answerPreCheckoutQuery(Update update) {
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
        answerPreCheckoutQuery.setOk(true);
        return answerPreCheckoutQuery;
    }

    //Отправка сообщения об успешной оплате
    public SendMessage successMesage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Thank you for your 'payment'! Don't worry, your imaginary credit card was not charged. " +
                "Your order is not on the way");
        return sendMessage;
    }

    //Вариант быстрой доставки
    private ShippingOption fastDelivery() {
        LabeledPrice quicklyLabeledPrice = new LabeledPrice("Quickly", 500);
        return new ShippingOption("Quickly", "Fast Delivery",
                Arrays.asList(quicklyLabeledPrice));
    }

    //Вариант обычной доставки
    private ShippingOption regularDelivery() {
        LabeledPrice regularLabaledPrice = new LabeledPrice("Regular", 300);
        return new ShippingOption("Regular", "Regular Delivery",
                Arrays.asList(regularLabaledPrice));
    }

    //Вытаскиваем наименование товаров, их стоимость и добавляем в счёт
    private ArrayList<LabeledPrice> setProducts(String[] products) {
        ArrayList<LabeledPrice> result = new ArrayList<>();
        for (int i = 0; i < products.length; i++) {
            String[] temp = products[i].split("-");
            int cost = (int) Float.parseFloat(temp[1]) * 100;
            result.add(new LabeledPrice(temp[0], cost));
        }
        return result;
    }
}
