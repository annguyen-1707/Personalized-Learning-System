package vn.fu_ohayo.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GrammarRequest {
    @NotEmpty(message = ErrorEnum.NOT_EMPTY_TITLE)
    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    private String titleJp;

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_STRUCTURE)
    @Size(max = 100, message = ErrorEnum.MAX_LENGTH_100)
    private String structure;

    @NotEmpty(message = ErrorEnum.NOT_EMPTY_MEANING)
    @Size(max = 200, message = ErrorEnum.MAX_LENGTH_200)
    private String meaning;

    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String usage;

    @Size(max = 500, message = ErrorEnum.MAX_LENGTH_500)
    private String example;

    @NotNull(message = ErrorEnum.NOT_EMPTY_JLPT_LEVEL)
    private JlptLevel jlptLevel;
}
