package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.AddFavoriteFolderRequest;
import vn.fu_ohayo.dto.request.FavoriteListDetailRequest;
import vn.fu_ohayo.dto.request.FavoriteListRequest;
import vn.fu_ohayo.dto.response.FavoriteDetailResponse;
import vn.fu_ohayo.dto.response.FlashCardStatusResponse;
import vn.fu_ohayo.dto.response.FlashcardResponse;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.FlashcardEnum;

import java.util.List;


public interface FavoriteListService {
    List<FlashcardResponse> getAllFlashcards(String type, long favoriteListId);
    void updateFlashCardStatus(String type, long favoriteListId, int flashcardId, FlashcardEnum status);
    public void copyFavoriteFolder(User user, Long folderId);
    Page<FolderFavoriteResponse> getAllFavoriteLists(String username, FavoriteListRequest request);
    void createFavoriteFolder(User username, AddFavoriteFolderRequest request);
    void updateFavoriteFolder(Long id, AddFavoriteFolderRequest request);
    void deleteFavoriteFolder(Long id);
    FavoriteDetailResponse getFavoriteFolderById(boolean exclude, Long folderId,
                                                 FavoriteListDetailRequest req);
    FlashCardStatusResponse getFlashCardStatus(int favoriteListId);
    void addItemToFavoriteList(long favoriteListId, String type, int itemId);
}
