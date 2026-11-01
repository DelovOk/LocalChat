package ru.Novoselov_Leonid.LocalChat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "room-members")
public interface RoomMemberRepository extends CrudRepository<RoomMember, Long> {
    List<RoomMember> findByUserId(Long userId);
    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);
    List<RoomMember> findByRoomIdAndIsBannedFalse(Long roomId);
    List<RoomMember> findByRoomIdAndIsBannedTrue(Long roomId);
    List<RoomMember> findByRoomId(Long roomId);
    void deleteByRoomId(Long roomId);
}
