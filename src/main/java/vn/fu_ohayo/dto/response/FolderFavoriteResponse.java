package vn.fu_ohayo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FolderFavoriteResponse {
    private Integer id;
    private String name;
    private boolean isPublic;
    private Date addedAt;
    private int numberOfVocabulary;
    private int numberOfGrammar;
    private String ownerName;
}
