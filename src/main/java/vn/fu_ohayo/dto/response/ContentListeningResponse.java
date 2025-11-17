package vn.fu_ohayo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.CategoryListeningEnum;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.JlptLevel;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentListeningResponse {
    private long contentListeningId;
    private String title;
    private String image;
    private CategoryListeningEnum category;
    private Date createdAt;
    private Date updatedAt;
    private String scriptJp;
    private String scriptVn;
    private String audioFile;
    private JlptLevel jlptLevel;
    private ContentStatus status;
}
