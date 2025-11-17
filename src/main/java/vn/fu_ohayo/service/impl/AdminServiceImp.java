package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminCreateAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminFilterAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminUpdateAdminRequest;
import vn.fu_ohayo.dto.response.admin.AdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminCheckUsernameAdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminFilterAdminResponse;
import vn.fu_ohayo.entity.Admin;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.AdminMapper;
import vn.fu_ohayo.repository.AdminRepository;
import vn.fu_ohayo.service.AdminService;
import vn.fu_ohayo.service.RoleService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final RoleService roleService;

    @Override
    public Page<SuperAdminFilterAdminResponse> filterAdminsForSuperAdmin(
            SuperAdminFilterAdminRequest request
    ) {
        return adminRepository.filterAdmins(
                request.getUsername(),
                request.getRoles(),
                PageRequest.of(
                        request.getCurrentPage(),
                        request.getPageSize()
                )
        ).map(adminMapper::toSuperAdminFilterAdminResponse);
    }

    @Override
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorEnum.ADMIN_NOT_FOUND));
        admin.setDeleted(true);
        adminRepository.save(admin);
    }

    @Override
    public AdminResponse createAdmin(SuperAdminCreateAdminRequest request) {
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorEnum.USERNAME_EXIST);
        }

        Admin admin = adminMapper.toAdmin(request);

        admin.setRoles(roleService.getRoles(request.getRoles()));

        return adminMapper.toAdminResponse(adminRepository.save(admin));
    }

    @Override
    public void recoverAdmin(Long adminId) {
        Admin admin = adminRepository.findByIdIncludingDeleted(adminId)
                .orElseThrow(() -> new AppException(ErrorEnum.ADMIN_NOT_FOUND));
        admin.setDeleted(false);
        adminRepository.save(admin);
    }

    @Override
    public SuperAdminCheckUsernameAdminResponse checkUsernameExists(String username) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsernameIncludingDeleted(username);

        if (optionalAdmin.isPresent()) {
            return adminMapper.toSuperAdminCheckUsernameAdminResponse(
                    optionalAdmin.get()
            );
        }

        return new SuperAdminCheckUsernameAdminResponse();
    }

    @Override
    public AdminResponse updateAdmin(Long adminId, SuperAdminUpdateAdminRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorEnum.ADMIN_NOT_FOUND));

        if(request.getPassword() == null || request.getPassword().isBlank()) {
            request.setPassword(admin.getPassword());
        }

        adminMapper.updateAdminFromSuperAdminRequest(admin, request);

        admin.setRoles(roleService.getRoles(request.getRoles()));

        return adminMapper.toAdminResponse(adminRepository.save(admin));
    }
}
