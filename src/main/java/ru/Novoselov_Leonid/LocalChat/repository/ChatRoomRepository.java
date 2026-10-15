package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByInviteCode(String inviteCode);
    List<ChatRoom> findByTitleContainingIgnoreCase(String titlePart);
    List<ChatRoom> findByIdInOrderByUpdatedAtDesc(List<Long> roomIds);
}
