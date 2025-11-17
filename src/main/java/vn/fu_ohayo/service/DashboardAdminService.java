package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.DashboardCouseraRateResponse;
import vn.fu_ohayo.dto.response.DashboardOverviewResponse;
import vn.fu_ohayo.dto.response.DashboardMonthlyUserCountResponse;

import java.util.List;

public interface DashboardAdminService {
    List<DashboardCouseraRateResponse> countCouseraRate (List<Integer> idSubjects);
    List<DashboardMonthlyUserCountResponse> countTotalUserEachMonth(int year);
    DashboardOverviewResponse countOverview();

}
