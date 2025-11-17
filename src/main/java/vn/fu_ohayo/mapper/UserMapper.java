package vn.fu_ohayo.mapper;

import org.mapstruct.*;
import vn.fu_ohayo.dto.DTO.ParentStudentDTO;
import vn.fu_ohayo.dto.DTO.StudentDTO;
import vn.fu_ohayo.dto.request.admin.user.AdminCreateUserRequest;
import vn.fu_ohayo.dto.request.admin.user.AdminUpdateUserRequest;
import vn.fu_ohayo.dto.response.admin.user.AdminCheckEmailUserResponse;
import vn.fu_ohayo.dto.DTO.AdminDTO;
import vn.fu_ohayo.dto.response.admin.user.AdminFilterUserResponse;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.entity.Admin;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "user", source = "student")
    List<StudentDTO> toStudentOnlyDtoList(List<ParentStudent> psList);

    @Mapping(target = "user", source = "parent")
    List<ParentStudentDTO> toParentOnlyDtoList(List<ParentStudent> psList);

    @Mapping(source = "role.name", target = "roleName")
    UserResponse toUserResponse(User user);

    @Mapping(source = "roles", target = "roles")
    AdminDTO toAdmin(Admin admin);

    User toUser(AdminCreateUserRequest addUserRequest);

    AdminFilterUserResponse toAdminFilterUserResponse(User user);

    void updateUserFromAdminRequest(@MappingTarget User user, AdminUpdateUserRequest request);

    AdminCheckEmailUserResponse toAdminCheckEmailUserResponseWithoutExist(User user);
    default AdminCheckEmailUserResponse toAdminCheckEmailUserResponse(User user) {
        AdminCheckEmailUserResponse res = toAdminCheckEmailUserResponseWithoutExist(user);
        res.setEmailExists(true);
        res.setDeleted(user.isDeleted());
        return res;
    }
}
