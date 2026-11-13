package ru.Novoselov_Leonid.LocalChat;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.Novoselov_Leonid.LocalChat.controller.CustomController;
import ru.Novoselov_Leonid.LocalChat.entity.Message;
import ru.Novoselov_Leonid.LocalChat.entity.RoomMember;
import ru.Novoselov_Leonid.LocalChat.enums.MemberRole;
import ru.Novoselov_Leonid.LocalChat.exception.ExceptionControllerAdvice;
import ru.Novoselov_Leonid.LocalChat.repository.MessageRepository;
import ru.Novoselov_Leonid.LocalChat.repository.RoomMemberRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomControllerTest {
    @Mock
    private RoomMemberRepository roomMemberRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private CustomController customController;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.standaloneSetup(customController)
                        .setControllerAdvice(new ExceptionControllerAdvice())
                        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                        .build()
        );
    }

    @Test
    void getMemberSuccess() {
        RoomMember member = new RoomMember();
        member.setId(10L);
        member.setRole(MemberRole.ADMIN);
        when(roomMemberRepository.findByRoomIdAndUserId(1L, 2L)).thenReturn(Optional.of(member));

        given().queryParam("roomId", 1L).queryParam("userId", 2L)
                .when().get("/custom/member")
                .then().statusCode(200).body("id", equalTo(10), "role", equalTo("ADMIN"));
    }

    @Test
    void getMemberNotFound() {
        when(roomMemberRepository.findByRoomIdAndUserId(1L, 99L)).thenReturn(Optional.empty());

        given().queryParam("roomId", 1L).queryParam("userId", 99L)
                .when().get("/custom/member")
                .then().statusCode(404);
    }

    @Test
    void getMemberMissingParam() {
        given().queryParam("roomId", 1L)
                .when().get("/custom/member")
                .then().statusCode(500);
    }

    @Test
    void getMessagesSuccess() {
        Message msg = new Message();
        msg.setId(100L);
        msg.setContent("Hello");
        when(messageRepository.findByRoomIdOrderBySentAtDesc(eq(1L), any()))
                .thenReturn(new SliceImpl<>(List.of(msg), PageRequest.of(0, 10), false));

        given().queryParam("roomId", 1L)
                .when().get("/custom/messages")
                .then().statusCode(200).body("content", hasSize(1), "content[0].content", equalTo("Hello"));
    }

    @Test
    void getMessagesEmpty() {
        when(messageRepository.findByRoomIdOrderBySentAtDesc(eq(5L), any()))
                .thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), false));

        given().queryParam("roomId", 5L)
                .when().get("/custom/messages")
                .then().statusCode(200).body("content", empty());
    }

    @Test
    void testError() {
        given()
                .when().get("/custom/error")
                .then().statusCode(404).body("message", equalTo("User or room with this id not found"));
    }
}
