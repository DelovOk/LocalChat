package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Novoselov_Leonid.LocalChat.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
}
