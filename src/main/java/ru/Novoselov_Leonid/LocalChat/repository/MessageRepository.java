package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Novoselov_Leonid.LocalChat.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "messages")
public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.room.id = :roomId ORDER BY m.sentAt DESC")
    Slice<Message> findByRoomIdOrderBySentAtDesc(Long roomId, Pageable pageable);
    List<Message> findByRoomIdAndSentAtAfterOrderBySentAtAsc(Long roomId, LocalDateTime joinedAt);
    List<Message> findByRoomIdAndContentContainingIgnoreCaseOrderBySentAtDesc(Long roomId, String text);
    Slice<Message> findBySenderIdOrderBySentAtDesc(Long senderId, Pageable pageable);
    Slice<Message> findByRoomIdAndSenderIdOrderBySentAtDesc(Long roomId, Long senderId, Pageable pageable);
    void deleteByRoomId(Long roomId);
}
