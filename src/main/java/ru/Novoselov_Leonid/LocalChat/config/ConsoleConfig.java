package ru.Novoselov_Leonid.LocalChat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.Novoselov_Leonid.LocalChat.command.CommandProcessor;

import java.util.Scanner;

// @Configuration
public class ConsoleConfig {

    private final CommandProcessor commandProcessor;

    public ConsoleConfig(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Bean
    public CommandLineRunner commandScanner() {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Введите 'help' для списка команд или 'exit' для выхода.");
                while (true) {
                    System.out.print("> ");
                    String input = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Выход из программы...");
                        System.exit(0);
                    }
                    commandProcessor.processCommand(input);
                }
            }
        };
    }
}
