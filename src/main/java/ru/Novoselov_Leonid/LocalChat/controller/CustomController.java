package ru.Novoselov_Leonid.LocalChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;
import ru.Novoselov_Leonid.LocalChat.entity.Message;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.repository.ChatRoomRepository;
import ru.Novoselov_Leonid.LocalChat.repository.MessageRepository;
import ru.Novoselov_Leonid.LocalChat.repository.RoomMemberRepository;

@RestController
@RequestMapping("/custom")
public class CustomController {
    @Autowired
    private RoomMemberRepository roomMemberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/member")
    public ResponseEntity<RoomMember> getMember(@RequestParam("roomId") Long roomId, @RequestParam("userId") Long userId) {
        return roomMemberRepository.findByRoomIdAndUserId(roomId, userId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/messages")
    public Slice<Message> getMessagesWithSender(@RequestParam("roomId") Long roomId, Pageable pageable) {
        return messageRepository.findByRoomIdOrderBySentAtDesc(roomId, pageable);
    }

    @GetMapping("/error")
    public void testError() {
        throw new ResourceNotFoundException("User or room with this id not found");
    }
}
