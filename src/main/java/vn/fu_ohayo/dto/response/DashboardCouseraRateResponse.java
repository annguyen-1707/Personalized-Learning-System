package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DashboardCouseraRateResponse {
    int subjectId;
    String subjectName;
    double couseraCompleteRate;
    int totalUserJoin;
}
