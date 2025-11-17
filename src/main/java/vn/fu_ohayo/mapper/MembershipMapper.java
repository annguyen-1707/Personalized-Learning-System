package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.MembershipResponse;
import vn.fu_ohayo.entity.MembershipLevel;


@Mapper(componentModel = "spring")
public interface MembershipMapper {
    MembershipResponse toMembershipResponse(MembershipLevel membership);
}
