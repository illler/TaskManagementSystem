package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.UserRepository;
import com.example.taskmanagementsystem.services.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // Ensure that the mocks are properly initialized
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        // Arrange
        String username = "test@example.com";
        User user = new User();
        user.setEmail(username);
        user.setPassword("password");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonexistent@example.com";

        // Ensure that the userRepository mock is not null
        assertNotNull(userRepository);

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        // Verify that the repository method is called as expected
        verify(userRepository, times(1)).findByEmail(username);
    }
}
