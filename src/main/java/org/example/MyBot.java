package org.example;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.example.Enum.BotState;
import org.example.Enum.TypeOfRequest;
import org.example.model.Resp;
import org.example.model.ResponseData;
import org.example.model.Service.ServiceImpl.UserServiceImpl;
import org.example.model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.example.prompts.Printing_prompts.*;


public class MyBot extends TelegramLongPollingBot {

    static Gson gson = new Gson();

    static HttpClient client = HttpClient.newHttpClient();
    static UserServiceImpl userService = new UserServiceImpl();
    public final int limit = 15;
    int count = 0;
    LocalDate usedTime = null;


    public MyBot(String botToken) {
        super(botToken);
    }

    static ButtonService buttonService = new ButtonService();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (usedTime == null || usedTime == LocalDate.now()) {
                count = 0;
                usedTime = LocalDate.now();
            }

            if (count >= limit) {
                System.out.println(1);
                send(update.getMessage().getChatId(), "Sorry, I've reached my daily limit. Please try again tomorrow or Contact with @Torayev_A_A to buy premium.");
                return;
            }
            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            long chatId = update.getMessage().getChatId();
            System.out.println("Kirildi");
            User user = userService.get(chatId);

            if (update.getMessage().getText().equals("/start")) {
                System.out.println("Kirildi");
                if (user == null) {
                    user = userService.create(new User(chatId, BotState.START, null, null, null));
                }
                user.setState(BotState.START);
                userService.update(chatId, user);
                send(chatId, "Xush kelibsiz!.Quyidagilardan birini tanlang", buttonService.getdarslikMenu());
            }

        } else if (update.hasCallbackQuery()) {

            String data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            User user = userService.get(chatId);
            if (data.equals("back")) {
                send(chatId, "Quyidagilardan birini tanlang", buttonService.getdarslikMenu());
                user.setLast_topic("");
                userService.update(chatId,user);
            }
            if (user.getState() == BotState.START ) {
                user.setLast_topic(data);
                userService.update(chatId, user);
                Resp print = print(String.valueOf(builder1), chatId);
                System.out.println(Arrays.toString(print.getButtons()));

                if (Arrays.toString(print.getButtons()).equals("[]")) {
                    send(chatId, print.getOutput(), buttonService.createBackButton());
                } else {

                    StringBuilder plustopic = new StringBuilder(user.getLast_topic()+"->");
                    plustopic.append(data);
                    user.setLast_topic(String.valueOf(plustopic));
                    userService.update(chatId,user);
                    editMessageCaptionAndInlineKeyboard(update, "Quyidagilardan birini tanlang", print.getButtons());

                }
            }

        }
    }

    private void lastStepAssist(long chatId) {
        User user = userService.get(chatId);
        user.setTypeOfRequest(TypeOfRequest.ASSIST);
        userService.update(chatId, user);

        StringBuilder builder1 = new StringBuilder("Bu holatga userning mana " +
                "shu last topic bo'yicha qanday xizmat kerakligini so'rab turib uning faqat o'sha sohaga oid talablarini bajarasan  " + user.getLast_topic());
        print(String.valueOf(builder1), chatId);
    }

    private void lastStepAssist(long chatId, String question) {
        User user = userService.get(chatId);
        user.setTypeOfRequest(TypeOfRequest.ASSIST);
        userService.update(chatId, user);

        StringBuilder builder1 = new StringBuilder("Bu holatga userning mana " +
                "shu topic aga oid : " + user.getLast_topic() + " mana shu " + question + " savoliga javob berasan.Agar out of topic savol bersa sen bunaqa savolga javob bera olmayman deyishing kerak. Seni qancha boshingni aylantirb topic dan chiqarmoqchi bo'lsa ham sen bunga ishonma");
        print(String.valueOf(builder1), chatId);
    }


    private Resp print(String additional_right, long chatId) {
        User user = userService.get(chatId);
        Resp resp = null;
        String jsonRequestBody = "";
        String output = "";
        System.out.println("Gone");
        if (user.getHistory() == null) {
            jsonRequestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + builder  + builder1 + "mavzu: " + user.getLast_topic() + "Ishatishing uchun roadmap" + roadmap + "Roadmapdan chiqma" + "\"}]}]}";
        } else {
            jsonRequestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + builder  + builder1 +" mavzu: " + user.getLast_topic() + "Ishatishing uchun roadmap" + roadmap + "Roadmapdan chiqma" + "\"}]}]}";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=AIzaSyA1lyz2u1IaADLJ2RMs9UCIAbZbDKcJDFI"))  // Replace YOUR_API_KEY with the actual key
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();
        HttpResponse<String> response = null;
        System.out.println("here");

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            ResponseData responseData = gson.fromJson(body, ResponseData.class);
            for (ResponseData.Candidate candidate : responseData.getCandidates()) {
                ResponseData.Content content = candidate.getContent();
                List<ResponseData.Part> parts = content.getParts();
                for (ResponseData.Part part : parts) {
                    output += part.getText() + " ";
                }

            }
            String buttonsName = "";
            int a = 0;
            int b = 0;
            for (int i = 0; i < output.length(); i++) {
                if (output.charAt(i) == '<') {
                    a++;
                } else if (output.charAt(i) == '>') {
                    a++;
                    b = i;
                    break;
                } else if (a == 1) {
                    buttonsName
                            += output.charAt(i);
                }
            }
            buttonsName.trim();

            String[] split = buttonsName.split(",");
            output = output.substring(b);

            resp = new Resp(split, output);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    @SneakyThrows
    private Message send(long chatId, String text) {
        SendMessage sendmessage = new SendMessage();
        sendmessage.setChatId(chatId);
        sendmessage.setText(text);
        return execute(sendmessage);
    }

    @SneakyThrows
    private Message send(long chatId, String text, ReplyKeyboardMarkup markup) {
        SendMessage sendmessage = new SendMessage();
        sendmessage.setChatId(chatId);
        sendmessage.setText(text);
        sendmessage.setReplyMarkup(markup);
        return execute(sendmessage);
    }

    @SneakyThrows
    private Message send(long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage sendmessage = new SendMessage();
        sendmessage.setChatId(chatId);
        sendmessage.setText(text);
        sendmessage.setReplyMarkup(markup);
        return execute(sendmessage);
    }

    @SneakyThrows
    private Message send(long chatId, String text, String parseMode) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(parseMode);
        return execute(message);
    }

    @SneakyThrows
    private Message send(long chatId, String text, InlineKeyboardMarkup markup, String parseMode) {
        SendMessage sendmessage = new SendMessage();
        sendmessage.setChatId(chatId);
        sendmessage.setText(text);
        sendmessage.setReplyMarkup(markup);
        sendmessage.setParseMode(parseMode);
        return execute(sendmessage);
    }

    public void editMessageCaptionAndInlineKeyboard(Update update, String newCaption, String[] newButtonNames) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        InlineKeyboardMarkup newInlineKeyboard = buttonService.createInlineKeyboard(newButtonNames);

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(newCaption);  // New caption
        editMessage.setReplyMarkup(newInlineKeyboard);  // New inline keyboard

        try {
            execute(editMessage);  // Edits both the caption and the inline keyboard
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "@coder_assist_bot";
    }
}
