package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.stereotype.Component;
import ru.Novoselov_Leonid.LocalChat.entity.User;

import java.util.List;

@Component
public class UserRepository implements CrudRepository<User, Long> {
    private final List<User> userList;

    public UserRepository(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void create(User user) {
        userList.add(user);
    }

    @Override
    public User read(Long id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(User user) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(user.getId())) {
                userList.set(i, user);
            }
        }
    }

    @Override
    public void delete(Long id) {
        userList.removeIf(user -> user.getId().equals(id));
    }

    public User findByNick(String nick) {
        return userList.stream()
                .filter(user -> user.getNick().equals(nick))
                .findFirst()
                .orElse(null);
    }
}
