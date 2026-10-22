package ru.Novoselov_Leonid.LocalChat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Novoselov_Leonid.LocalChat.dao.CustomQueryDao;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.UserStatus;
import ru.Novoselov_Leonid.LocalChat.repository.ChatRoomRepository;
import ru.Novoselov_Leonid.LocalChat.repository.RoomMemberRepository;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
public class RepositoryAndCriteriaTest {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final CustomQueryDao customQueryDao;

    private User testUser;
    private ChatRoom testRoom;
    private RoomMember testMember;

    @Autowired
    public RepositoryAndCriteriaTest(UserRepository userRepository, ChatRoomRepository chatRoomRepository, RoomMemberRepository roomMemberRepository, CustomQueryDao customQueryDao) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.roomMemberRepository = roomMemberRepository;
        this.customQueryDao = customQueryDao;
    }

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setNick("user" + UUID.randomUUID().toString().substring(0, 5));
        testUser.setLogin("login" + UUID.randomUUID().toString().substring(0, 5));
        testUser.setPassword("password");
        testUser.setStatus(UserStatus.ONLINE);

        userRepository.save(testUser);

        testRoom = new ChatRoom();
        testRoom.setTitle("Room");
        testRoom = chatRoomRepository.save(testRoom);

        testMember = new RoomMember();
        testMember.setUser(testUser);
        testMember.setRoom(testRoom);
        testMember = roomMemberRepository.save(testMember);
    }

    @Test
    void testUserRepositoryFindByProfileStatus() {
        List<User> users = userRepository.findByStatus(UserStatus.ONLINE);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertTrue(users.stream().anyMatch(u -> u.getId().equals(testUser.getId())));
    }

    @Test
    void testCriteriaDaoFindUsersByProfileStatus() {
        List<User> users = customQueryDao.findUsersByProfileStatus(UserStatus.ONLINE);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertTrue(users.stream().anyMatch(u -> u.getId().equals(testUser.getId())));
    }

    @Test
    void testRoomMemberRepositoryFindByRoomIdAndUserId() {
        Optional<RoomMember> member = roomMemberRepository.findByRoomIdAndUserId(testRoom.getId(), testUser.getId());
        Assertions.assertTrue(member.isPresent());
        Assertions.assertEquals(testMember.getId(), member.get().getId());
    }

    @Test
    void testCriteriaDaoFindMemberByRoomIdAndUserId() {
        Optional<RoomMember> member = customQueryDao.findMemberByRoomIdAndUserId(testRoom.getId(), testUser.getId());
        Assertions.assertTrue(member.isPresent());
        Assertions.assertEquals(testMember.getId(), member.get().getId());
    }
}
