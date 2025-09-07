package com.example;

import com.example.dto.UserDto;
import com.example.model.User;
import com.example.repository.UserDao;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Test
    void createUser_shouldCallDaoSave() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        userService.createUser("Alice", "alice@example.com", 22);

        verify(mockDao, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnDtos() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setAge(30);

        when(mockDao.findAll()).thenReturn(List.of(user));

        List<UserDto> dtos = userService.getAllUsers();
        assertEquals(1, dtos.size());
        assertEquals("Bob", dtos.get(0).name());
        verify(mockDao, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnDtoIfFound() throws IllegalAccessException {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Charlie");
        when(user.getEmail()).thenReturn("charlie@example.com");
        when(user.getAge()).thenReturn(28);

        when(mockDao.findById(1L)).thenReturn(user);
        UserDto dto = userService.getUserById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("Charlie", dto.name());
        assertEquals("charlie@example.com", dto.email());
        assertEquals(28, dto.age());
        verify(mockDao, times(1)).findById(1L);
    }

    @Test
    void getUserById_shouldReturnNullIfNotFound() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        when(mockDao.findById(99L)).thenReturn(null);

        assertNull(userService.getUserById(99L));
        verify(mockDao, times(1)).findById(99L);
    }

    @Test
    void updateUser_shouldCallDaoUpdateIfUserExists() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        User user = mock(User.class);
        when(mockDao.findById(1L)).thenReturn(user);

        userService.updateUser(1L, "Travis", "travis@example.com", 35);

        verify(user).setName("Travis");
        verify(user).setEmail("travis@example.com");
        verify(user).setAge(35);
        verify(mockDao, times(1)).update(user);

    }

    @Test
    void updateUser_shouldNotCallUpdateIfUserNotFound() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        when(mockDao.findById(99L)).thenReturn(null);

        userService.updateUser(99L, "Name", "email@example.com", 40);

        verify(mockDao, never()).update(any(User.class));
    }

    @Test
    void deleteUser_shouldCallDaoDeleteIfUserExists() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        User user = mock(User.class);
        when(mockDao.findById(1L)).thenReturn(user);

        userService.deleteUser(1L);

        verify(mockDao, times(1)).delete(user);
    }

    @Test
    void deleteUser_shouldNotCallDeleteIfUserNotFound() {
        UserDao mockDao = mock(UserDao.class);
        UserService userService = new UserService(mockDao);

        when(mockDao.findById(99L)).thenReturn(null);

        userService.deleteUser(99L);

        verify(mockDao, never()).delete(any(User.class));
    }
}
