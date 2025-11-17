package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.admin.user.AdminCreateUserRequest;
import vn.fu_ohayo.dto.request.admin.user.AdminFilterUserRequest;
import vn.fu_ohayo.dto.request.admin.user.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.response.admin.user.AdminCheckEmailUserResponse;
import vn.fu_ohayo.dto.response.admin.user.AdminFilterUserResponse;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.service.UserService;

import static vn.fu_ohayo.constant.ConstantGolbal.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<Page<AdminFilterUserResponse>> filterUsers(
            @Valid @ModelAttribute AdminFilterUserRequest request
    ) {
        return ApiResponse.<Page<AdminFilterUserResponse>>builder()
                .code(READ_SUCCESS_CODE)
                .status(READ_SUCCESS_MESSAGE)
                .message("Get user by filter success")
                .data(userService.filterUsersForAdmin(request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(
            @PathVariable Long userId
    ) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(DELETE_SUCCESS_CODE)
                .status(DELETE_SUCCESS_MESSAGE)
                .message("Delete user success")
                .build();
    }

    @PatchMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid AdminUpdateUserRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .code(UPDATE_SUCCESS_CODE)
                .status(UPDATE_SUCCESS_MESSAGE)
                .message("Update user success")
                .data(userService.updateUser(userId, request))
                .build();
    }

    @PatchMapping("/{userId}/recover")
    public ApiResponse<UserResponse> recoverUser(
            @PathVariable Long userId
    ) {
        userService.recoverUser(userId);
        return ApiResponse.<UserResponse>builder()
                .code(RECOVER_SUCCESS_CODE)
                .status(RECOVER_SUCCESS_MESSAGE)
                .message("Recover user success")
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(
            @RequestBody @Valid AdminCreateUserRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .code(CREATE_SUCCESS_CODE)
                .status(CREATE_SUCCESS_MESSAGE)
                .message("Create user success")
                .data(userService.createUser(request))
                .build();
    }

    @GetMapping("/check-email")
    public ApiResponse<AdminCheckEmailUserResponse> checkEmail(
            @RequestParam String email
    ) {
        return ApiResponse.<AdminCheckEmailUserResponse>builder()
                .code(READ_SUCCESS_CODE)
                .status(READ_SUCCESS_MESSAGE)
                .message("Check email success")
                .data(userService.checkEmailExists(email))
                .build();
    }
}
