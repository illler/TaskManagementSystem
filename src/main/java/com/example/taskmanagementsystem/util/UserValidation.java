package com.example.taskmanagementsystem.util;

import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.services.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidation implements Validator {

    private final MyUserDetailsService myUserDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String email = (String) target;
        try {
            myUserDetailsService.loadUserByUsername(email);
        }catch (UsernameNotFoundException ignored){
            return;
        }

        errors.rejectValue("email", "","Человек с таким email уже существует");
    }
}
