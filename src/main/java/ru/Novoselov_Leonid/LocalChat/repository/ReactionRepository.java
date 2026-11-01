package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Novoselov_Leonid.LocalChat.entity.Reaction;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "reactions")
public interface ReactionRepository extends CrudRepository<Reaction, Long> {
    List<Reaction> findByMessageId(Long messageId);
    Optional<Reaction> findByMessageIdAndUserIdAndCode(Long messageId, Long userId, String code);
    List<Reaction> findByMessageIdAndUserId(Long messageId, Long userId);
}
