package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashCardStatusResponse {
    int countNotLearned;
    int countInProgress;
    int countMastered;
    List<Integer> flashcardMasteredIds;
}
