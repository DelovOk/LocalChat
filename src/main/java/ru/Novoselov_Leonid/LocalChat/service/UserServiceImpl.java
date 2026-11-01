package ru.Novoselov_Leonid.LocalChat.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.UserRole;
import ru.Novoselov_Leonid.LocalChat.enums.UserStatus;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;

import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void createUser(String login, String password, String nick) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("User with this login already exists");
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setNick(nick);
        user.setStatus(UserStatus.ONLINE);
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
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
        return userRepository.findByNickContainingIgnoreCase(nick).getFirst();
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        updateUser(id, user -> user.setPassword(newPassword));
    }

    @Override
    public User findByLogin(String login) { return userRepository.findByLogin(login).orElse(null); }

    private void updateUser(Long id, Consumer<User> updater) {
        userRepository.findById(id).ifPresent(user -> {
            updater.accept(user);
            userRepository.save(user);
        });
    }
}