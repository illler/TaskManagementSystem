package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.DTO.UserDTO;
import com.example.taskmanagementsystem.auth.AuthenticationRequest;
import com.example.taskmanagementsystem.auth.AuthenticationResponse;
import com.example.taskmanagementsystem.auth.RegisterRequest;
import com.example.taskmanagementsystem.error.ErrorResponse;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.taskmanagementsystem.util.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "AuthController")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserValidation userValidation;



    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request,
                                                           BindingResult bindingResult){
        userValidation.validate(request.getEmail(), bindingResult);
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Пользователь уже создан"));
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
