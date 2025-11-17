package vn.fu_ohayo.dto.response;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentResponse {
    private long contentId;
    private ContentTypeEnum contentType;
}
