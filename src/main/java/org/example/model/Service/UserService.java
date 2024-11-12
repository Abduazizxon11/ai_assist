package org.example.model.Service;

import org.example.model.User;

import java.util.List;

public interface UserService {


    User create(User user);
    User get(long chatId);
    List<User> getAll();
    User update(long chatId, User user);
}
