package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.auth.AuthenticationRequest;
import com.example.taskmanagementsystem.auth.AuthenticationResponse;
import com.example.taskmanagementsystem.auth.RegisterRequest;
import com.example.taskmanagementsystem.model.Role;
import com.example.taskmanagementsystem.model.Token;
import com.example.taskmanagementsystem.model.TokenType;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.TokenRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;

    @Mock
    TokenRepository tokenRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthenticationService authenticationService;

    User user;

    Token token;

    @BeforeEach
    void init(){
        user = User.builder()
                .id("1234")
                .username("Alex")
                .email("0250901@gmail.com")
                .password("1234")
                .role(Role.AUTHOR)
                .build();
        token = Token.builder()
                .user(user)
                .token("123412341234")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }


    @Test
    @DisplayName("Test register")
    void testRegister_whenUserTryToRegister_returnTokenAndUserId(){


        RegisterRequest request = RegisterRequest.builder()
                .username("Alex")
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        when(repository.save(any(User.class))).thenReturn(user);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        when(jwtService.generateToken(any(User.class))).thenReturn("123412341234");

        AuthenticationResponse authenticationResponse = authenticationService.register(request);

        assertNotNull(authenticationResponse, "Response should not be null");
        assertEquals(token.getToken(), authenticationResponse.getToken(), "Token before and after registration must be the same");
        assertNotEquals(user.getPassword(), "password", "Password before and after registration should not be the same");
    }

    @Test
    @DisplayName("Test login")
    void testAuthenticate_whenUserTryToLogin_returnTokenAndUserId(){
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("0250901@gmail.com")
                .password("1234")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(tokenRepository.saveAll(anyList())).thenReturn(List.of(token));

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("123412341234");
        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of(token));


        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

        assertNotNull(authenticationResponse, "Response should not be null");
        assertEquals(token.getToken(), authenticationResponse.getToken(), "Token before and after login must be the same");

    }

}