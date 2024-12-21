package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot("7582921639:AAG74ev_wGQJx6zYEqXvrTKBFq4tUSPD9QA"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}