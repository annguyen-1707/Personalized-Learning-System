package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteDetailResponse {
    private int favoriteId;
    private String favoriteListName;
    private boolean isPublic;
    private Date createdAt;

    private String ownerName;

    private Page<VocabularyResponse> vocabularyList;
    private Page<GrammarResponse> grammarList;
}
