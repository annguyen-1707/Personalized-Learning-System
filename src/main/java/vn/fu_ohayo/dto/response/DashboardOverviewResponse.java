package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.entity.SystemLog;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardOverviewResponse {
    int totalUserNow;
    int totalUserCompareLastMonth;
    int totalSubjectNow;
    int totalSubjectCompareLastMonth;
    int totalLessonNow;
    int totalLessonCompareLastMonth;
    double completionRateSubjectNow;
    double completionRateSubjectCompareLastMonth;
    List<CountUserEachMembership>  countUserEachMembership;
    List<SystemLog> systemLogs;
}
