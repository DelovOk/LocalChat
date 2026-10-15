package ru.Novoselov_Leonid.LocalChat.dao;

import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.UserStatus;

import java.util.List;
import java.util.Optional;

public interface CustomQueryDao {
    Optional<RoomMember> findMemberByRoomIdAndUserId(Long roomId, Long userId);
    List<User> findUsersByProfileStatus(UserStatus status);
}
