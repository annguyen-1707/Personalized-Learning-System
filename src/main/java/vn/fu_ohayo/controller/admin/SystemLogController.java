package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.request.superAdmin.systemLog.SystemLogRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.superAdmin.systemLog.SystemLogResponse;
import vn.fu_ohayo.service.SystemLogService;

import static vn.fu_ohayo.constant.ConstantGolbal.READ_SUCCESS_CODE;
import static vn.fu_ohayo.constant.ConstantGolbal.READ_SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/super-admin/system-logs")
public class SystemLogController {

    private final SystemLogService systemLogService;

    @GetMapping
    public ApiResponse<Page<SystemLogResponse>> filterSystemLogs(
            @Valid @ModelAttribute SystemLogRequest request) {
        return ApiResponse.<Page<SystemLogResponse>>builder()
                .code(READ_SUCCESS_CODE)
                .status(READ_SUCCESS_MESSAGE)
                .message("Get system log by filter success")
                .data(systemLogService.filterSystemLogsForAdmin(request))
                .build();
    }
}
