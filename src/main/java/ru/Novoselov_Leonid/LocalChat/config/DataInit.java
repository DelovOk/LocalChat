package ru.Novoselov_Leonid.LocalChat.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;
import ru.Novoselov_Leonid.LocalChat.entity.Message;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.repository.ChatRoomRepository;
import ru.Novoselov_Leonid.LocalChat.repository.MessageRepository;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Iterator;

@Configuration
public class DataInit {
    @Bean
    public CommandLineRunner initChatData(UserRepository userRepository, ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        return args -> {
            if (messageRepository.count() > 0) {
                return;
            }
            User user = userRepository.findById(1L).orElseThrow();
            ChatRoom room = null;
            Iterator<ChatRoom> iterator = chatRoomRepository.findAll().iterator();
            if (iterator.hasNext()) {
                room = iterator.next();
            } else {
                ChatRoom newRoom = new ChatRoom();
                newRoom.setTitle("Chat");
                newRoom.setDescription("Chat");
                room = chatRoomRepository.save(newRoom);
            }
            Message msg1 = new Message();
            msg1.setContent("Text 1");
            msg1.setSentAt(LocalDateTime.now());
            msg1.setEdited(false);
            msg1.setSender(user);
            msg1.setRoom(room);
            Message msg2 = new Message();
            msg2.setContent("Text 2");
            msg2.setSentAt(LocalDateTime.now());
            msg2.setEdited(false);
            msg2.setSender(user);
            msg2.setRoom(room);
            Message msg3 = new Message();
            msg3.setContent("Text 3");
            msg3.setSentAt(LocalDateTime.now());
            msg3.setEdited(false);
            msg3.setSender(user);
            msg3.setRoom(room);
            messageRepository.save(msg1);
            messageRepository.save(msg2);
            messageRepository.save(msg3);
        };
    }
}
