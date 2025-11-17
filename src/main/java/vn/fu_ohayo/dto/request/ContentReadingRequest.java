package vn.fu_ohayo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryReadingEnum;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentReadingRequest {
    @NotBlank(message = ErrorEnum.NOT_EMPTY_TITLE)
    private String title;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AVATAR)
    private String image;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CATEGORY)
    private CategoryReadingEnum category;

    @NotBlank(message = ErrorEnum.NOT_EMPTY_SCRIPT)
    private String scriptJp;

    @NotBlank(message = ErrorEnum.NOT_EMPTY_SCRIPT)
    private String scriptVn;

    @Size(max = 255, message = ErrorEnum.INVALID_URL_AUDIO)
    private String audioFile;

    @NotNull(message = ErrorEnum.NOT_EMPTY_DATE)
    @PastOrPresent(message = "Time can not over today")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date timeNew;

    @Enumerated(EnumType.STRING)
    private JlptLevel jlptLevel;

}
