package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

public class ButtonService {
    public InlineKeyboardMarkup menu() {
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData("LexusUz");
        inlineKeyboardButton.setText("Start");
        row.add(inlineKeyboardButton);
        rowList.add(row);
        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getmainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton("mashqlar"));row1.add(new KeyboardButton("assistent"));
        row2.add(new KeyboardButton("Orqaga"));
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
    public InlineKeyboardMarkup createInlineKeyboard(String[] buttonNames) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        for (int i = 0; i < buttonNames.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonNames[i]);
            button.setCallbackData( buttonNames[i]);

            currentRow.add(button);


            if (currentRow.size() == 2 || i == buttonNames.length - 1) {
                keyboardRows.add(new ArrayList<>(currentRow));
                currentRow.clear();
            }
        }

        inlineKeyboardMarkup.setKeyboard(keyboardRows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getdarslikMenu() {
        String[] topics = {
                "Linux", "Git", "IDE", "Core", "Collections",
                "Advanced", "Exception Handling", "Streams & Functional Programming",
                "Testing", "Databases", "Clean Code", "Logging",
                "Multi Threading", "Build Tools", "HTTP", "Frameworks"
        };

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (int i = 0; i < topics.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(topics[i]);
            button.setCallbackData(topics[i]);

            row.add(button);

            if (row.size() == 2 || i == topics.length - 1) {
                rows.add(new ArrayList<>(row));
                row.clear();
            }
        }

        markup.setKeyboard(rows);
        return markup;
    }
}
