package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.MyUserDetailsService;

@Component
@Qualifier("userValidator")
public class UserValidator implements Validator {
    private final MyUserDetailsService userDetailsService;
    @Autowired
    public UserValidator(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User targetUser = (User) target;
        System.out.println("------------>In validate(Object target, Errors errors): User: "
                + targetUser.toString());
        if (userDetailsService.isUserExistByEmail(targetUser.getEmail(), targetUser.getId())) {
            errors.rejectValue("email", "01", "This email is taken");
        }
    }
}
