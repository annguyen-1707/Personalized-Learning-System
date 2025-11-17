package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.FlashcardEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlashcardResponse {
    private Long id;
    private String frontText;         // vocab: kanji; grammar: titleJp
    private String subText;           // vocab: furigana; grammar: structure
    private String translation;       // vocab: meaning; grammar: meaning
    private String notes;             // vocab: description; grammar: usage
    private String jlptLevel;
    private String romaji;            // chỉ vocab
    private String partOfSpeech;      // chỉ vocab
    private FlashcardEnum status;
}
