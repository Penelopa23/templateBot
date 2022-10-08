package templatebot.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 *
 * Класс обработки сообщений
 *
 */

@Slf4j
@Component
@AllArgsConstructor
public class MessageHandler {

    public SendMessage startMessage(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(),
                "*Let's get started*\uD83C\uDF54\n\nPlease tap the button below to order you perfect burger");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(new InlineKeyBoardBot().webAppKeyboard());
       return sendMessage;
    }
}
