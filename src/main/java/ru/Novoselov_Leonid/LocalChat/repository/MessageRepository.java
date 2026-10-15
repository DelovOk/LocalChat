package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Novoselov_Leonid.LocalChat.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByRoomIdOrderBySentAtAsc(Long roomId);
    List<Message> findByRoomIdAndSentAtAfterOrderBySentAtAsc(Long roomId, LocalDateTime joinedAt);
    List<Message> findByRoomIdAndContentContainingIgnoreCaseOrderBySentAtDesc(Long roomId, String text);
    void deleteByRoomId(Long roomId);
}
