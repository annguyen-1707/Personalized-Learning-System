package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.entity.QuizQuestion;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VocabularyResponse {

    private String vocabularyId;

    private String kanji;

    private String kana;

    private String romaji;

    private String meaning;

    private String description;

    private String example;

    private PartOfSpeech partOfSpeech;

    private JlptLevel jlptLevel;

    private Date updatedAt;

    private QuizQuestionResponse quizQuestion;
}
