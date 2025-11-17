package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminCreateAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminFilterAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminUpdateAdminRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.admin.AdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminCheckUsernameAdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminFilterAdminResponse;
import vn.fu_ohayo.service.AdminService;

import static vn.fu_ohayo.constant.ConstantGolbal.*;
import static vn.fu_ohayo.constant.ConstantGolbal.READ_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/super-admin/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ApiResponse<Page<SuperAdminFilterAdminResponse>> filterAdmins(
            @Valid @ModelAttribute SuperAdminFilterAdminRequest request
    ) {
        return ApiResponse.<Page<SuperAdminFilterAdminResponse>>builder()
                .code(READ_SUCCESS_CODE)
                .status(READ_SUCCESS_MESSAGE)
                .message("Get admin by filter success")
                .data(adminService.filterAdminsForSuperAdmin(request))
                .build();
    }

    @DeleteMapping("/{adminId}")
    public ApiResponse<Void> deleteAdmin(
            @PathVariable Long adminId
    ) {
        adminService.deleteAdmin(adminId);
        return ApiResponse.<Void>builder()
                .code(DELETE_SUCCESS_CODE)
                .status(DELETE_SUCCESS_MESSAGE)
                .message("Delete admin success")
                .build();
    }

    @PostMapping
    public ApiResponse<AdminResponse> createAdmin(
            @RequestBody @Valid SuperAdminCreateAdminRequest  request
    ) {
        return ApiResponse.<AdminResponse>builder()
                .code(CREATE_SUCCESS_CODE)
                .status(CREATE_SUCCESS_MESSAGE)
                .message("Create admin success")
                .data(adminService.createAdmin(request))
                .build();
    }

    @PatchMapping("/{adminId}")
    public ApiResponse<AdminResponse> updateAdmin(
            @PathVariable("adminId") Long adminId,
            @RequestBody @Valid SuperAdminUpdateAdminRequest request
    ) {
        return ApiResponse.<AdminResponse>builder()
                .code(UPDATE_SUCCESS_CODE)
                .status(UPDATE_SUCCESS_MESSAGE)
                .message("Update admin success")
                .data(adminService.updateAdmin(adminId, request))
                .build();
    }

    @PatchMapping("/{adminId}/recover")
    public ApiResponse<AdminResponse> recoverAdmin(
            @PathVariable Long adminId
    ) {
        adminService.recoverAdmin(adminId);
        return ApiResponse.<AdminResponse>builder()
                .code(RECOVER_SUCCESS_CODE)
                .status(RECOVER_SUCCESS_MESSAGE)
                .message("Recover admin success")
                .build();
    }

    @GetMapping("/check-username")
    public ApiResponse<SuperAdminCheckUsernameAdminResponse> checkUsername(
            @RequestParam String username
    ) {
        return ApiResponse.<SuperAdminCheckUsernameAdminResponse>builder()
                .code(READ_SUCCESS_CODE)
                .status(READ_SUCCESS_MESSAGE)
                .message("Check username success")
                .data(adminService.checkUsernameExists(username))
                .build();
    }
}
