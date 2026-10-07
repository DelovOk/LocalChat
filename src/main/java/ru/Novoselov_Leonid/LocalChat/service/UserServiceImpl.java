package ru.Novoselov_Leonid.LocalChat.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.Novoselov_Leonid.LocalChat.config.AppPropertiesConfig;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;

import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AppPropertiesConfig appPropertiesConfig;

    public UserServiceImpl(UserRepository userRepository, AppPropertiesConfig appPropertiesConfig) {
        this.userRepository = userRepository;
        this.appPropertiesConfig = appPropertiesConfig;
    }
    @Override
    public void createUser(Long id, String login, String password, String nick) {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setNick(nick);
        userRepository.create(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.read(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void updateLogin(Long id, String newLogin) {
        updateUser(id, user -> user.setLogin(newLogin));
    }

    @Override
    public void updateNick(Long id, String newNick) {
        updateUser(id, user -> user.setNick(newNick));
    }

    @Override
    public User findByNick(String nick) {
        return userRepository.findByNick(nick);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        updateUser(id, user -> user.setPassword(newPassword));
    }

    private void updateUser(Long id, Consumer<User> updater) {
        User user = userRepository.read(id);
        if (user != null) {
            updater.accept(user);
            userRepository.update(user);
        }
    }

    @PostConstruct
    public void printAppInfo() {
        System.out.println("Приложение: " + appPropertiesConfig.getAppName());
        System.out.println("Версия:     " + appPropertiesConfig.getAppVersion());
    }
}
