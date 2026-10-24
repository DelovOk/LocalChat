package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "chat-rooms")
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByInviteCode(String inviteCode);
    List<ChatRoom> findByTitleContainingIgnoreCase(String titlePart);
    List<ChatRoom> findByIdInOrderByUpdatedAtDesc(List<Long> roomIds);
}
