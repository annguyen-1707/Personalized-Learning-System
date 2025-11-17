package vn.fu_ohayo.dto.response;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.FavoriteListGrammar;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.QuizQuestion;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class QuizGrammarResponse {

    private int grammarId;

    private String titleJp;

    private String structure;

    private String meaning;

    private String usage;

    private String example;

    private JlptLevel jlptLevel;

    private String quizQuestion;

}
