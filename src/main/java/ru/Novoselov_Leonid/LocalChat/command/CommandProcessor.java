package ru.Novoselov_Leonid.LocalChat.command;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.service.UserService;


// @Component
public class CommandProcessor {
    private final UserService userService;

    public CommandProcessor(UserService userService) {
        this.userService = userService;
    }

    public void processCommand(String input) {
        String[] cmd = input.split(" ");
        String action = cmd[0].toLowerCase();
        try {
            switch (action) {
                case "create" -> {
                    if (cmd.length != 5) {
                        System.out.println("Ошибка! Формат команды: create <id> <login> <password> <nick>");
                        return;
                    }
                    userService.createUser(Long.valueOf(cmd[1]), cmd[2], cmd[3], cmd[4]);
                    System.out.println("Пользователь успешно добавлен.");
                }

                case "find" -> {
                    if (cmd.length != 2) {
                        System.out.println("Ошибка! Формат команды: find <id | nick>");
                        return;
                    }
                    String query = cmd[1];
                    User user;
                    try {
                        Long id = Long.parseLong(query);
                        user = userService.findById(id);
                    } catch (NumberFormatException e) {
                        user = userService.findByNick(query);
                    }

                    if (user != null) {
                        System.out.println("Найден пользователь с: id=" + user.getId() + " login=" + user.getLogin() + " nick=" + user.getNick());
                    } else {
                        System.out.println("Пользователь не найден.");
                    }
                }

                case "update" -> {
                    if (cmd.length != 4) {
                        System.out.println("Ошибка! Формат команды: update <login | password | nick> <id> <новое_значение>");
                        return;
                    }
                    Long id = Long.valueOf(cmd[2]);
                    User user = userService.findById(id);
                    if (user == null) {
                        System.out.println("Пользователь не найден.");
                        return;
                    }
                    String field = cmd[1].toLowerCase();
                    switch (field) {
                        case "login" -> {
                            userService.updateLogin(id, cmd[3]);
                            System.out.println("Логин обновлен.");
                        }
                        case "password" -> {
                            userService.updatePassword(id, cmd[3]);
                            System.out.println("Пароль обновлен.");
                        }
                        case "nick" -> {
                            userService.updateNick(id, cmd[3]);
                            System.out.println("Ник обновлен.");
                        }
                        default -> {
                            System.out.println("Неизвестное поле " + field);
                        }
                    }
                }

                case "delete" -> {
                    if (cmd.length != 2) {
                        System.out.println("Ошибка! Формат команды: delete <id>");
                        return;
                    }
                    userService.deleteById(Long.valueOf(cmd[1]));
                    System.out.println("Пользователь удален.");
                }

                case "help" -> {
                    System.out.println("""
                        Доступные команды:
                          create <id> <login> <password> <nick>                     - создать пользователя
                          find <id | nick>                                          - найти по ID или нику
                          update <login | password | nick> <id> <новое_значение>    - изменить логин
                          delete <id>                                               - удалить по ID
                          help                                                      - открыть список команд
                          exit                                                      - выйти из программы
                    """);
                }

                default -> {
                    System.out.println("Неизвестная комадна.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! ID должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
