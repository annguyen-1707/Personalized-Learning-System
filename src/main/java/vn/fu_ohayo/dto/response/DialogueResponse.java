package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.ContentStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DialogueResponse {

    private long dialogueId;

    private ContentSpeakingResponse contentSpeaking;

    private String questionJp;

    private String questionVn;

    private String answerVn;

    private String answerJp;

    private Date createdAt;

    private Date updatedAt;

    private ContentStatus status;

}
