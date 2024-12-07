package life.liquide.trades.service;

import life.liquide.trades.entity.User;
import life.liquide.trades.model.LoginUserDto;
import life.liquide.trades.model.RegisterUserDto;
import life.liquide.trades.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Input DTOs
        registerUserDto = new RegisterUserDto();
        registerUserDto.setFullName("Test User");
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setPassword("password123");

        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("password123");

        // User entity
        user = User.builder()
                .fullName("Test User")
                .email("test@example.com")
                .password("encodedPassword").build();
    }

    @Test
    void testSignup_Success() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = authenticationService.signup(registerUserDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.getFullName());
        assertEquals("test@example.com", result.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSignup_Failure_WhenSaveFails() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.signup(registerUserDto));
        assertEquals("Database error", exception.getMessage());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAuthenticate_Success() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        authenticationService.authenticate(loginUserDto);

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testAuthenticate_Failure_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(loginUserDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testAuthenticate_Failure_InvalidCredentials() {
        // Arrange
        doThrow(new RuntimeException("Invalid credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(loginUserDto));
        assertEquals("Invalid credentials", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(any());
    }
}
