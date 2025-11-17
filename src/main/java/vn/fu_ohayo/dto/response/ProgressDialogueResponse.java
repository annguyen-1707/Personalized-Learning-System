package vn.fu_ohayo.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.entity.PronunciationResult;
import vn.fu_ohayo.enums.ProgressStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProgressDialogueResponse {
    private int progressId;

    private PronunciationResult pronunciationResult;

    private Integer version;

    private Date createdAt;

    private ProgressStatus progressStatus;

}
