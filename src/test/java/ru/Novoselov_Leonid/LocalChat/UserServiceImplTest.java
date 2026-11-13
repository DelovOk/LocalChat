package ru.Novoselov_Leonid.LocalChat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.enums.UserRole;
import ru.Novoselov_Leonid.LocalChat.enums.UserStatus;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;
import ru.Novoselov_Leonid.LocalChat.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserSuccess() {
        String login = "user";
        String rawPassword = "password";
        String encodedPassword = "encoded_password";
        String nick = "user";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        userService.createUser(login, rawPassword, nick);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(login, savedUser.getLogin());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(nick, savedUser.getNick());
        assertEquals(UserStatus.ONLINE, savedUser.getStatus());
        assertEquals(UserRole.USER, savedUser.getRole());
    }

    @Test
    void createUserLoginAlreadyExists() {
        String existingLogin = "user";

        when(userRepository.findByLogin(existingLogin)).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(existingLogin, "1234", "user")
        );
        assertEquals("User with this login already exists", exception.getMessage());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByIdUserExists() {
        User user = new User();
        user.setId(1L);
        user.setLogin("user");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user", result.getLogin());
    }

    @Test
    void findByIdUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        User result = userService.findById(99L);

        assertNull(result);
    }

    @Test
    void findByLoginUserExists() {
        User user = new User();
        user.setLogin("user");

        when(userRepository.findByLogin("user")).thenReturn(Optional.of(user));
        User result = userService.findByLogin("user");

        assertNotNull(result);
        assertEquals("user", result.getLogin());
    }

    @Test
    void findByLoginUserNotFound() {
        when(userRepository.findByLogin("unknown")).thenReturn(Optional.empty());

        assertNull(userService.findByLogin("unknown"));
    }

    @Test
    void findByNickUsersFound() {
        User firstUser = new User();
        firstUser.setNick("user1");
        User secondUser = new User();
        secondUser.setNick("user2");

        when(userRepository.findByNickContainingIgnoreCase("user"))
                .thenReturn(List.of(firstUser, secondUser));
        User result = userService.findByNick("user");

        assertEquals("user1", result.getNick());
    }

    @Test
    void findByNickNoUsersFound() {
        when(userRepository.findByNickContainingIgnoreCase("unknown"))
                .thenReturn(Collections.emptyList());

        assertThrows(
                NoSuchElementException.class,
                () -> userService.findByNick("unknown")
        );
    }

    @Test
    void updateLoginUserExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setLogin("oldLogin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        userService.updateLogin(1L, "newLogin");

        assertEquals("newLogin", existingUser.getLogin());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateNickUserExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setNick("oldNick");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        userService.updateNick(1L, "newNick");

        assertEquals("newNick", existingUser.getNick());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updatePasswordUserExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        userService.updatePassword(1L, "newPass");

        assertEquals("newPass", existingUser.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateNickUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        userService.updateNick(99L, "newNick");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteByIdCallsRepositoryDelete() {
        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }
}