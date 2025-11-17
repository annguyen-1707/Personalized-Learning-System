package vn.fu_ohayo.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategorySpeakingEnum;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentSpeakingRequest {
    @NotBlank(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;
    @NotBlank(message = ErrorEnum.NOT_EMPTY_IMAGE)
    private String image;
    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    private CategorySpeakingEnum category;
    @Enumerated(EnumType.STRING)
    private JlptLevel jlptLevel;
}
