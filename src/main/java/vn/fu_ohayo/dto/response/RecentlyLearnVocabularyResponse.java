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
public class RecentlyLearnVocabularyResponse {
    private int progressId;
    private VocabularyResponse vocabulary;
    private ProgressStatus progressStatus;
    private Date reviewedAt;
}
