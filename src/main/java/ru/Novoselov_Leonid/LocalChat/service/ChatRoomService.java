package ru.Novoselov_Leonid.LocalChat.service;

import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;

public interface ChatRoomService {
    ChatRoom createRoomWithOwner(String title, Long ownerUserId);
}
