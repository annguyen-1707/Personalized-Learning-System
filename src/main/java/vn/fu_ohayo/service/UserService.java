package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.admin.user.AdminCreateUserRequest;
import vn.fu_ohayo.dto.request.admin.user.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.admin.user.AdminFilterUserRequest;
import vn.fu_ohayo.dto.request.user.UserRegister;
import vn.fu_ohayo.dto.response.admin.user.AdminCheckEmailUserResponse;
import vn.fu_ohayo.dto.response.admin.user.AdminFilterUserResponse;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.entity.UserProfileDTO;

import java.util.List;

public interface UserService {
    UserProfileDTO getUserProfileDTO(String email);
    List<UserResponse> getAllUsers();
    ApiResponse<?> registerInitial(InitialRegisterRequest initialRegisterRequest);
    UserResponse completeProfile(CompleteProfileRequest completeProfileRequest, String email);
    UserResponse registerUser(UserRegister userRegister);

    Page<AdminFilterUserResponse> filterUsersForAdmin(AdminFilterUserRequest request);
    void deleteUser(Long userId);
    void recoverUser(Long userId);
    UserResponse updateUser(Long userId, AdminUpdateUserRequest request);
    UserResponse createUser(AdminCreateUserRequest request);
    AdminCheckEmailUserResponse checkEmailExists(String email);

    User getUserByEmail(String email);
    User getUserById(Long userId);
    String updateAvatar(String email, String avatarUrl);
    UserResponse getUser();
}