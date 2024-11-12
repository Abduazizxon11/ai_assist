package org.example;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.example.Enum.BotState;
import org.example.model.ResponseData;
import org.example.model.Service.ServiceImpl.UserServiceImpl;
import org.example.model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class MyBot extends TelegramLongPollingBot {

    static Gson gson = new Gson();

    static HttpClient client = HttpClient.newHttpClient();
    static UserServiceImpl userService = new UserServiceImpl();

    public MyBot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {




        if (update.hasMessage() && update.getMessage().hasText()) {


            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();
            ButtonService buttonService = new ButtonService();

            long chatId = update.getMessage().getChatId();
            System.out.println("Kirildi");
            User user = userService.get(chatId);

            if (update.getMessage().getText().equals("/start")) {
                System.out.println("Kirildi");
                if (user == null) {

                    user = userService.create(new User(chatId, BotState.START, null));


                }


                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Xush kelibsiz!.Quyidagilardan birini tanlang");
                message.setReplyMarkup(buttonService.menu());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

                user.setState(BotState.START);
                userService.update(chatId, user);


            }
            if (user.getState() == BotState.SECOND) {
                String jsonRequestBody = "";
                String output = "";

                System.out.println("Gone");

                if (user.getHistory() == null) {
                    jsonRequestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + "savol: " + update.getMessage().getText()  + "\"}]}]}";

                } else {

                    jsonRequestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + " savol: " + update.getMessage().getText() + " tarix:" + user.getHistory().get(user.getHistory().size() - 1) + "\"}]}]}";


                }


                // Create API request with API key as query parameter
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyA1lyz2u1IaADLJ2RMs9UCIAbZbDKcJDFI"))  // Replace YOUR_API_KEY with the actual key
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))  // Send the JSON body
                        .build();

                // Send request and get response
                HttpResponse<String> response = null;
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Get the response body
                String responseBody = response.body();

                // Parse JSON response
                ResponseData responseData = gson.fromJson(responseBody, ResponseData.class);


                for (ResponseData.Candidate candidate : responseData.getCandidates()) {
                    ResponseData.Content content = candidate.getContent();
                    List<ResponseData.Part> parts = content.getParts();
                    for (ResponseData.Part part : parts) {
                        output += part.getText() + " ";  // This will print the text part
                    }
                }

                List<String> user1 = new ArrayList<>();
                user1.add(output + " " + update.getMessage().getText());
                user.setHistory(user1);
                userService.update(chatId, user);

                SendMessage message = new SendMessage();
                message.setText(output);
                message.setChatId(chatId);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }


            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            long chatId = update.getCallbackQuery().getMessage().getChatId();
            ButtonService buttonService = new ButtonService();

            User user = userService.get(chatId);


            if (update.getCallbackQuery().getData().equals("LexusUz") && user.getState() == BotState.START) {


                EditMessageText editMessageCaption = new EditMessageText();
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setText("Malumot kiritng:");
                editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editMessageText.setChatId(chatId);


                try {
                    execute(editMessageText);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                user.setState(BotState.SECOND);
                userService.update(chatId, user);

            }


        }


    }

    @Override
    public String getBotUsername() {
        return "LexusGetInfoBot";
    }
}
