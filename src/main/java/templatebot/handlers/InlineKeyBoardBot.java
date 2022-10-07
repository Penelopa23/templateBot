package templatebot.handlers;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyBoardBot {


    public InlineKeyboardMarkup webAppKeyboard() { //создание клавиатуры с webapp кнопкой
        return getButtons(); //возвращаем клавиатуру
    }

    /**
     * Формируем линию кнопок и возвращаем в клаивиатуру
     */
    public InlineKeyboardMarkup getButtons() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(getButton("Test Page", getWebAppInfo("https://penelopa23.github.io/testsite/")));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    /**
     * Создаём кнопку
     */
    private List<InlineKeyboardButton> getButton(String buttonName, WebAppInfo webAppInfo) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setWebApp(webAppInfo);
        button.setText(buttonName);
        //button.setCallbackData(dataButton);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }

    /**
     * Создаём WebAppInfo -формат хранения url
     */
    private WebAppInfo getWebAppInfo(String url) {
        return new WebAppInfo(url);
    }
}
