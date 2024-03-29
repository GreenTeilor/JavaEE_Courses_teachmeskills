package by.teachmeskills.controllers;

import by.teachmeskills.constants.PagesPaths;
import by.teachmeskills.constants.RequestAttributesNames;
import by.teachmeskills.constants.SessionAttributesNames;
import by.teachmeskills.entities.User;
import by.teachmeskills.services.UserService;
import by.teachmeskills.services.implementation.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login")
@SessionAttributes({SessionAttributesNames.USER, SessionAttributesNames.CART})
public class LoginController {
    private static final UserService userService = new UserServiceImpl();

    @GetMapping
    public ModelAndView openLoginPage() {
        return new ModelAndView(PagesPaths.LOGIN_PAGE);
    }

    @PostMapping
    public ModelAndView login(@ModelAttribute(RequestAttributesNames.USER) User user, Model model) {
        return userService.getUser(user.getEmail(), user.getPassword(), model);
    }

    @ModelAttribute(RequestAttributesNames.USER)
    public User initializeUserInSession() {
        return new User();
    }
}
