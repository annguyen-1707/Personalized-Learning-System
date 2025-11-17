package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminCreateAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminFilterAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminUpdateAdminRequest;
import vn.fu_ohayo.dto.response.admin.AdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminCheckUsernameAdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminFilterAdminResponse;

public interface AdminService {

    Page<SuperAdminFilterAdminResponse> filterAdminsForSuperAdmin(
            SuperAdminFilterAdminRequest request
    );

    void deleteAdmin(Long adminId);

    AdminResponse createAdmin(
            SuperAdminCreateAdminRequest request
    );

    void recoverAdmin(Long adminId);

    SuperAdminCheckUsernameAdminResponse checkUsernameExists(String username);

    AdminResponse updateAdmin(Long adminId, SuperAdminUpdateAdminRequest request);
}
