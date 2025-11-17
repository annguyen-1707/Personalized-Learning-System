package vn.fu_ohayo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PatchGrammarRequest {
    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    private String titleJp;

    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    private String structure;

    @Size(max = 200, message = ErrorEnum.MAX_LENGTH_200)
    private String meaning;

    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String usage;

    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String example;

    private JlptLevel jlptLevel;

    private int lessonId;
}
