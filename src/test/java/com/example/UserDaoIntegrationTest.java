package com.example;

import com.example.model.User;
import com.example.repository.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgres=new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testDb")
            .withUsername("test")
            .withPassword("test");
    private UserDao userDao;
    @BeforeEach
    void setup(){
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        userDao = new UserDao();
    }
    @Test
    void testSaveAndFindById(){
        User user=new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setAge(25);

        userDao.save(user);
        assertNotNull(user.getId());

        User found =userDao.findById(user.getId());
        assertEquals("Alice",found.getName());
    }

    @Test
    void testFindAll() {
        userDao.findAll().forEach(userDao::delete);

        User user1 = new User();
        user1.setName("Bob");
        user1.setEmail("bob@example.com");
        user1.setAge(30);
        userDao.save(user1);

        User user2 = new User();
        user2.setName("Charlie");
        user2.setEmail("charlie@example.com");
        user2.setAge(28);
        userDao.save(user2);

        List<User> users=userDao.findAll();
        assertTrue(users.size() >= 2);
    }
    @Test
    void testUpdate() {
        User user = new User();
        user.setName("Daniel");
        user.setEmail("daniel@example.com");
        user.setAge(29);
        userDao.save(user);

        User beforeUpdate=userDao.findById(user.getId());

        user.setName("Craige");
        user.setEmail("craige@example.com");
        user.setAge(28);
        userDao.update(user);

        User afterUpdate=userDao.findById(user.getId());

        assertEquals(beforeUpdate.getId(),afterUpdate.getId());
        assertNotEquals(beforeUpdate.getName(),afterUpdate.getName());
        assertNotEquals(beforeUpdate.getEmail(),afterUpdate.getEmail());
        assertNotEquals(beforeUpdate.getAge(),afterUpdate.getAge());
    }
    @Test
    void testDelete() {
        User user1 = new User();
        user1.setName("Daniel");
        user1.setEmail("daniel@example.com");
        user1.setAge(29);
        userDao.save(user1);
        User beforeDelete=userDao.findById(user1.getId());
        userDao.delete(user1);
        User afterDelete=userDao.findById(user1.getId());
        assertNull(afterDelete);

    }

}
