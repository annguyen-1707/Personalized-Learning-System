package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.superAdmin.systemLog.SystemLogRequest;
import vn.fu_ohayo.dto.response.superAdmin.systemLog.SystemLogResponse;

public interface SystemLogService {
    Page<SystemLogResponse> filterSystemLogsForAdmin(SystemLogRequest request);
}
