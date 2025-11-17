package vn.fu_ohayo.dto.response;

import lombok.*;
import vn.fu_ohayo.enums.ProgressStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProgressGrammarsFlashCardResponse {
    private int grammarId;
    private ProgressStatus progressStatus;
}
