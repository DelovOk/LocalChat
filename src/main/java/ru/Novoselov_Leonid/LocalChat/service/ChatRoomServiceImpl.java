package ru.Novoselov_Leonid.LocalChat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.Novoselov_Leonid.LocalChat.entity.ChatRoom;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.MemberRole;
import ru.Novoselov_Leonid.LocalChat.repository.ChatRoomRepository;
import ru.Novoselov_Leonid.LocalChat.repository.RoomMemberRepository;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, RoomMemberRepository roomMemberRepository, UserRepository userRepository, PlatformTransactionManager transactionManager) {
        this.chatRoomRepository = chatRoomRepository;
        this.roomMemberRepository = roomMemberRepository;
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public ChatRoom createRoomWithOwner(String title, Long ownerUserId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            User owner = userRepository.findById(ownerUserId).orElseThrow(() -> new IllegalArgumentException("User not found"));

            ChatRoom room = new ChatRoom();
            room.setTitle(title);
            ChatRoom savedRoom = chatRoomRepository.save(room);

            RoomMember member = new RoomMember();
            member.setRoom(savedRoom);
            member.setUser(owner);
            member.setRole(MemberRole.ADMIN);
            roomMemberRepository.save(member);

            transactionManager.commit(status);
            return savedRoom;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
}
