package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fu_ohayo.dto.request.superAdmin.systemLog.SystemLogRequest;
import vn.fu_ohayo.dto.response.superAdmin.systemLog.SystemLogResponse;
import vn.fu_ohayo.mapper.SystemLogMapper;
import vn.fu_ohayo.repository.SystemLogRepository;
import vn.fu_ohayo.service.SystemLogService;

@Transactional
@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class SystemLogServiceImp implements SystemLogService {

    SystemLogRepository systemLogRepository;
    SystemLogMapper systemLogMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<SystemLogResponse> filterSystemLogsForAdmin(SystemLogRequest request) {
        return systemLogRepository.filterSystemLogs(
                request.getStartTimestamp(),
                request.getEndTimestamp(),
                request.getAction(),
                request.getDetails(),
                request.getRole(),
                PageRequest.of(
                        request.getCurrentPage(),
                        request.getPageSize(),
                        Sort.by(Sort.Direction.DESC, "timestamp"))
        ).map(systemLogMapper::toSystemLogResponse);
    }

}
