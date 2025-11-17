package vn.fu_ohayo.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryListeningEnum;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentListeningRequest {
    @NotBlank(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    private CategoryListeningEnum category;

    @NotBlank(message = ErrorEnum.NOT_EMPTY_SCRIPT)
    private String scriptJp;

    @NotBlank(message = ErrorEnum.NOT_EMPTY_SCRIPT)
    private String scriptVn;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AUDIO)
    private String audioFile;

    private JlptLevel jlptLevel;

}
