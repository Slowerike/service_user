package com.example.console;

import com.example.dto.UserDto;
import com.example.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService service = new UserService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== User Service Menu ===");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Показать пользователя по ID");
            System.out.println("4. Обновить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите email: ");
                    String email = scanner.nextLine();
                    System.out.print("Возраст: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    service.createUser(name, email, age);
                }
                case "2" -> {
                    List<UserDto> users = service.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("Пользователи отсутствуют");
                    } else {
                        users.forEach(System.out::println);
                    }
                }
                case "3" -> {
                    System.out.print("Введите ID пользователя: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    UserDto user = service.getUserById(id);
                    if (user != null) {
                        System.out.println(user);
                    } else {
                        System.out.println("Пользователь не найден");
                    }
                }
                case "4" -> {
                    System.out.print("Введите ID пользователя для обновления: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    System.out.print("Новое имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Новый email: ");
                    String email = scanner.nextLine();
                    System.out.print("Новый возраст: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    service.updateUser(id, name, email, age);
                }
                case "5" -> {
                    System.out.print("Введите ID пользователя для удаления: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    service.deleteUser(id);
                }
                case "0" -> {
                    System.out.println("Выход из программы...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}
