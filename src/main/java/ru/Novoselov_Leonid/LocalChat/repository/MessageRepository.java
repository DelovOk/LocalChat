package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import ru.Novoselov_Leonid.LocalChat.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Slice<Message> findByRoomIdOrderBySentAtDesc(Long roomId, Pageable pageable);
    List<Message> findByRoomIdAndSentAtAfterOrderBySentAtAsc(Long roomId, LocalDateTime joinedAt);
    List<Message> findByRoomIdAndContentContainingIgnoreCaseOrderBySentAtDesc(Long roomId, String text);
    Slice<Message> findBySenderIdOrderBySentAtDesc(Long senderId, Pageable pageable);
    Slice<Message> findByRoomIdAndSenderIdOrderBySentAtDesc(Long roomId, Long senderId, Pageable pageable);
    void deleteByRoomId(Long roomId);
}
