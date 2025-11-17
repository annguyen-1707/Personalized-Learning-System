package vn.fu_ohayo.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.fu_ohayo.dto.request.admin.user.AdminCreateUserRequest;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImp userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    // ========== Test deleteUser ==========
    @Test
    void deleteUser_whenUserExists_shouldMarkAsDeleted() {
        Long userId = 1L;
        User inputUser = new User();
        inputUser.setUserId(userId);
        inputUser.setDeleted(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(inputUser));

        userService.deleteUser(userId);

        assertTrue(inputUser.isDeleted());
        verify(userRepository).save(inputUser);
    }

    @Test
    void deleteUser_whenUserNotFound_shouldThrowException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () ->
                userService.deleteUser(userId));
        assertEquals(ErrorEnum.USER_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(ErrorEnum.USER_NOT_FOUND.getCode(), exception.getCode());
    }

    // ========== Test addUser ==========
    @Test
    void addUser_whenEmailExists_shouldThrowException() {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .email("test@example.com")
                .phone("0123456789")
                .build();

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () ->
                userService.createUser(request));
        assertEquals(ErrorEnum.EMAIL_EXIST.getMessage(), exception.getMessage());
        assertEquals(ErrorEnum.EMAIL_EXIST.getCode(), exception.getCode());
    }

    @Test
    void addUser_whenPhoneExists_shouldThrowException() {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .email("test@example.com")
                .phone("0123456789")
                .build();

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByPhone("0123456789")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () ->
                userService.createUser(request));
        assertEquals(ErrorEnum.PHONE_EXIST.getMessage(), exception.getMessage());
        assertEquals(ErrorEnum.PHONE_EXIST.getCode(), exception.getCode());
    }

    @Test
    void addUser_whenValid_shouldReturnUserResponse() {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .email("test@example.com")
                .phone("0123456789")
                .build();

        User userEntity = new User();
        UserResponse userResponse = new UserResponse();

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByPhone("0123456789")).thenReturn(false);
        when(userMapper.toUser(request)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserResponse(userEntity)).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertEquals(userResponse, result);
    }
}
