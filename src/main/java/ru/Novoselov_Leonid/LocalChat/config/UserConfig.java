package ru.Novoselov_Leonid.LocalChat.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.Novoselov_Leonid.LocalChat.entity.User;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    public List<User> userList() {
        return new ArrayList<>();
    }
}
