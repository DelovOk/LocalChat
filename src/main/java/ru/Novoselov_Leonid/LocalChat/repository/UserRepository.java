package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.UserStatus;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
    List<User> findByNickContainingIgnoreCase(String nickPart);
    List<User> findByStatus(UserStatus status);
}
