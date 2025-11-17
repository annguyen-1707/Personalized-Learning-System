package vn.fu_ohayo.controller.admin;

import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.DashboardCouseraRateResponse;
import vn.fu_ohayo.dto.response.DashboardOverviewResponse;
import vn.fu_ohayo.dto.response.DashboardMonthlyUserCountResponse;
import vn.fu_ohayo.service.DashboardAdminService;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardAdminController {
    private final DashboardAdminService dashboardAdminService;

    public DashboardAdminController(DashboardAdminService dashboardAdminService) {
        this.dashboardAdminService = dashboardAdminService;
    }

    @GetMapping("/overview")
    public ApiResponse<DashboardOverviewResponse> getDashboardOverview() {
        return ApiResponse.<DashboardOverviewResponse>builder()
                .code("200")
                .status("success")
                .data(dashboardAdminService.countOverview())
                .message("Get dashboard Overview successfully!")
                .build();
    }

    @PostMapping ("/couseraRate")
    public ApiResponse<List<DashboardCouseraRateResponse>> getDashboardCouseraRate(@RequestBody List<Integer> idSubjects) {
        return ApiResponse.<List<DashboardCouseraRateResponse>>builder()
                .code("200")
                .status("success")
                .data(dashboardAdminService.countCouseraRate(idSubjects))
                .message("Get dashboard Cousera Rate successfully!")
                .build();
    }

@GetMapping("/totalUserEachMonth")
    public ApiResponse<List<DashboardMonthlyUserCountResponse>> getDashboardTotalUserEachWeek(@RequestParam(value = "year", required = false) Integer year ) {
        if(year == null) {
            year = Year.now().getValue();
        }
        return ApiResponse.<List<DashboardMonthlyUserCountResponse>>builder()
                .code("200")
                .status("success")
                .data(dashboardAdminService.countTotalUserEachMonth(year))
                .message("Get dashboard Total User Each Month successfully!")
                .build();

}
}
