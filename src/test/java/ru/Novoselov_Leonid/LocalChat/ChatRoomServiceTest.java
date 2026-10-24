package ru.Novoselov_Leonid.LocalChat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.repository.ChatRoomRepository;
import ru.Novoselov_Leonid.LocalChat.repository.RoomMemberRepository;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;
import ru.Novoselov_Leonid.LocalChat.service.ChatRoomService;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class ChatRoomServiceTest {

    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;

    private User existingUser;

    @Autowired
    public ChatRoomServiceTest(ChatRoomService chatRoomService, ChatRoomRepository chatRoomRepository, RoomMemberRepository roomMemberRepository, UserRepository userRepository) {
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
        this.roomMemberRepository = roomMemberRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setLogin("login" + UUID.randomUUID().toString().substring(0, 5));
        user.setPassword("password");
        user.setNick("nick" + UUID.randomUUID().toString().substring(0, 5));
        existingUser = userRepository.save(user);
    }

    @Test
    void testCreateRoomWithOwnerSuccess() {
        ChatRoom createdRoom = chatRoomService.createRoomWithOwner("Room", existingUser.getId());

        Optional<ChatRoom> foundRoom = chatRoomRepository.findById(createdRoom.getId());
        Assertions.assertTrue(foundRoom.isPresent());
        Assertions.assertEquals("Room", foundRoom.get().getTitle());

        Optional<RoomMember> member = roomMemberRepository.findByRoomIdAndUserId(createdRoom.getId(), existingUser.getId());
        Assertions.assertTrue(member.isPresent());
        Assertions.assertEquals(existingUser.getId(), member.get().getUser().getId());
    }

    @Test
    void testCreateRoomWithOwnerRollback() {
        Long nonExistentUserId = 100L;
        long roomsCountBefore = chatRoomRepository.count();

        Assertions.assertThrows(Exception.class, () -> {
            chatRoomService.createRoomWithOwner("Room", nonExistentUserId);
        });

        long roomsCountAfter = chatRoomRepository.count();
        Assertions.assertEquals(roomsCountBefore, roomsCountAfter);
    }
}
