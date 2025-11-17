package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategorySpeakingEnum;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentSpeakingResponse {

    private long contentSpeakingId;
    private String title;
    private String image;
    private CategorySpeakingEnum category;
    private Date createdAt;
    private Date updatedAt;
    private JlptLevel jlptLevel;
    private ContentStatus status;

}
