package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminCreateAdminRequest;
import vn.fu_ohayo.dto.request.superAdmin.admin.SuperAdminUpdateAdminRequest;
import vn.fu_ohayo.dto.response.admin.AdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminCheckUsernameAdminResponse;
import vn.fu_ohayo.dto.response.superAdmin.admin.SuperAdminFilterAdminResponse;

import vn.fu_ohayo.entity.Admin;



@Mapper(componentModel = "spring")
public interface AdminMapper {
    SuperAdminFilterAdminResponse toSuperAdminFilterAdminResponse(Admin admin);
    AdminResponse toAdminResponse(Admin admin);
    Admin toAdmin(SuperAdminCreateAdminRequest request);
    void updateAdminFromSuperAdminRequest(@MappingTarget Admin admin, SuperAdminUpdateAdminRequest request);
    SuperAdminCheckUsernameAdminResponse toSuperAdminCheckUsernameAdminResponseWithoutExist(Admin admin);
    default SuperAdminCheckUsernameAdminResponse toSuperAdminCheckUsernameAdminResponse(Admin admin) {
        SuperAdminCheckUsernameAdminResponse res = toSuperAdminCheckUsernameAdminResponseWithoutExist(admin);
        res.setUsernameExists(true);
        res.setIsDeleted(admin.isDeleted());
        return res;
    }
}
