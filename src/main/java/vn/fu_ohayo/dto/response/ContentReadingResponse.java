package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryReadingEnum;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentReadingResponse {
    private long contentReadingId;
    private String title;
    private String image;
    private CategoryReadingEnum category;
    private Date createdAt;
    private Date updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date timeNew;
    private String scriptJp;
    private String scriptVn;
    private String audioFile;
    private JlptLevel jlptLevel;
    private ContentStatus status;
    private ContentResponse content;
}
