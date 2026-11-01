package ru.Novoselov_Leonid.LocalChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;
import ru.Novoselov_Leonid.LocalChat.repository.ChatRoomRepository;

@Controller
@RequestMapping("/custom/chat-rooms/view")
public class ChatRoomControllerView {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @GetMapping("/list")
    public String chatRoomListView(Model model) {
        Iterable<ChatRoom> rooms = chatRoomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "roomList";
    }
}
