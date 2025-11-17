package vn.fu_ohayo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteListRequest {
    private String viewType; // "mine" or "public"
    private Integer currentPage = 0;
    private Integer pageSize = 21;
    private String type;
    private Boolean isPublic;
    private String searchName;
}
