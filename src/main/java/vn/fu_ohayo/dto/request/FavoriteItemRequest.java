package vn.fu_ohayo.dto.request;

import lombok.Data;

@Data
public class FavoriteItemRequest {
    private int favoriteListId;
    private int itemId; // ID của Vocabulary hoặc Grammar
    private String type; // Loại: VOCABULARY hoặc GRAMMAR
}
