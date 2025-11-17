package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteListDetailRequest {
    private String type; // "vocabulary" or "grammar"
    private PartOfSpeech category;
    private JlptLevel jlptLevel;
    private Integer currentPage = 0;
    private Integer pageSize = 20;
    private String searchName;
}
