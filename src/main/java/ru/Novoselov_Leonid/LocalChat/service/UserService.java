package ru.Novoselov_Leonid.LocalChat.service;

import ru.Novoselov_Leonid.LocalChat.entity.User;

public interface UserService {
    void createUser(Long id, String login, String password, String nick);

    User findById(Long id);

    void deleteById(Long id);

    void updateLogin(Long id, String newLogin);

    void updateNick(Long id, String newNick);

    User findByNick(String nick);

    void updatePassword(Long id, String newPassword);
}
