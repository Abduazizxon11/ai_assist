package org.example.model.Service.ServiceImpl;

import org.example.Enum.BotState;
import org.example.model.Service.UserService;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList<>();


    @Override
    public User create(User user) {
        if (get(user.getChatId()) == null) {
            User newUser = new User(
                    user.getChatId(), user.getState(),null
           ,null ,null);
            users.add(newUser);
        }


        return user;
    }

    @Override
    public User get(long chatId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getChatId() == chatId) {

                return users.get(i);
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User update(long chatId, User user) {
        User oldUser = get(chatId);

        for (int i = 0; i < users.size(); i++) {
            if (oldUser.getChatId() == chatId) {
                oldUser.setState(user.getState());

                users.set(i, oldUser);
            }
        }
        for (int i = 0; i < users.size(); i++) {
            if (oldUser.getChatId() == chatId) {
                oldUser.setHistory(user.getHistory());

                users.set(i, oldUser);
            }
        }     for (int i = 0; i < users.size(); i++) {
            if (oldUser.getChatId() == chatId) {
                oldUser.setLast_topic(user.getLast_topic());

                users.set(i, oldUser);
            }
        }


        return oldUser;
    }
}
