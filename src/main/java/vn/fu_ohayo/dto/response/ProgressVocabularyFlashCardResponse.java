package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ProgressStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProgressVocabularyFlashCardResponse {
    private int vocabularyId;
    private ProgressStatus progressStatus;
}
