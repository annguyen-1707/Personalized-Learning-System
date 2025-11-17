package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.superAdmin.systemLog.SystemLogResponse;
import vn.fu_ohayo.entity.SystemLog;

@Mapper(componentModel = "spring")
public interface SystemLogMapper {
    SystemLogResponse toSystemLogResponse(SystemLog systemLog);
}
