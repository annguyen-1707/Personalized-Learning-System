package vn.fu_ohayo.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.LessonStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonResponse {

    private int lessonId;

    private String name;

    private String description;

    private String thumbnailUrl;

    private LessonStatus status;

    private String videoUrl;

    private List<VocabularyResponse> vocabularies;

    private List<GrammarResponse> grammars;
}
