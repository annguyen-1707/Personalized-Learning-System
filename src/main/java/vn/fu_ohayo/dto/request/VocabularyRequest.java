package vn.fu_ohayo.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VocabularyRequest {

    @NotNull(message = ErrorEnum.NOT_EMPTY_KANJI)
    private String kanji;

    @NotNull(message = ErrorEnum.NOT_EMPTY_KANA)
    @Size(max = 50, message = ErrorEnum.MAX_LENGTH_50)
    private String kana; // e.g., "こうこう" for 高校

    @NotNull(message = ErrorEnum.NOT_EMPTY_ROMAJI)
    @Size(max = 50, message = ErrorEnum.MAX_LENGTH_50)
    private String romaji; // e.g., "kōkō" for 高校

    @NotNull(message = ErrorEnum.NOT_EMPTY_MEANING)
    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    private String meaning;


    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String description;


    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String example;

    @Enumerated(EnumType.STRING)
    private PartOfSpeech partOfSpeech;


    @NotNull(message = ErrorEnum.NOT_EMPTY_JLPT_LEVEL)
    private JlptLevel jlptLevel;

    private int lessonId;

}
