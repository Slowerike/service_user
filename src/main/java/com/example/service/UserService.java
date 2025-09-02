package com.example.service;

import com.example.repository.UserDao;
import com.example.model.User;
import com.example.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao = new UserDao();

    // Создание пользователя
    public void createUser(String name, String email, int age) {
        logger.info("Создаём пользователя: {} / {}", name, email);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        userDao.save(user);
    }

    // Получение всех пользователей
    public List<UserDto> getAllUsers() {
        logger.info("Получаем всех пользователей");
        return userDao.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getName(), u.getEmail(), u.getAge(), u.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // Получение пользователя по ID
    public UserDto getUserById(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            logger.warn("Пользователь с id={} не найден", id);
            return null;
        }
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }

    // Обновление пользователя
    public void updateUser(Long id, String name, String email, int age) {
        User user = userDao.findById(id);
        if (user == null) {
            logger.warn("Невозможно обновить. Пользователь с id={} не найден", id);
            return;
        }
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        userDao.update(user);
        logger.info("Пользователь с id={} обновлён", id);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            logger.warn("Невозможно удалить. Пользователь с id={} не найден", id);
            return;
        }
        userDao.delete(user);
        logger.info("Пользователь с id={} удалён", id);
    }
}
