package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends CrudRepository<RoomMember, Long> {
    List<RoomMember> findByUserId(Long userId);
    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);
    List<RoomMember> findByRoomIdAndIsBannedFalse(Long roomId);
    List<RoomMember> findByRoomIdAndIsBannedTrue(Long roomId);
    List<RoomMember> findByRoomId(Long roomId);
    void deleteByRoomId(Long roomId);
}
