package ru.Novoselov_Leonid.LocalChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.service.UserService;

@Controller
public class RegistrationController
{
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm()
    {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(User user, Model model)
    {
        try
        {
            userService.createUser(user.getLogin(), user.getPassword(), user.getNick());
            return "redirect:/login";
        }
        catch (Exception ex)
        {
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }
    }
}
