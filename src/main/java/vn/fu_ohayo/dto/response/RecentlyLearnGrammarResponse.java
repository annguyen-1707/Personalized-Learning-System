package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ProgressStatus;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecentlyLearnGrammarResponse {
    private int progressId;
    private GrammarResponse grammar;
    private ProgressStatus progressStatus;
    private Date reviewedAt;
}
