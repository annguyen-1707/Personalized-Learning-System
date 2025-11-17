package vn.fu_ohayo.controller.favoriteList;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.AddFavoriteFolderRequest;
import vn.fu_ohayo.dto.request.FavoriteItemRequest;
import vn.fu_ohayo.dto.request.FavoriteListDetailRequest;
import vn.fu_ohayo.dto.request.FavoriteListRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.FavoriteDetailResponse;
import vn.fu_ohayo.dto.response.FolderFavoriteResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.service.FavoriteListService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteListController {

    private final FavoriteListService favoriteListService;

    @PostMapping("/{favoriteListId}/items")
    public ResponseEntity<String> addItemToFavoriteList(
            @PathVariable int favoriteListId,
            @RequestBody FavoriteItemRequest request) { // Re-using FavoriteItemRequest for itemId and type
        // Optional: you might want to verify that favoriteListId in path matches request.getFavoriteListId()
        // if (favoriteListId != request.getFavoriteListId()) { ... }
        favoriteListService.addItemToFavoriteList(favoriteListId, request.getType(), request.getItemId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Item added to favorite list successfully.");
    }

    @GetMapping("/{folderId}")
    public ApiResponse<FavoriteDetailResponse> getFavoriteDetails(
            @PathVariable Long folderId,
            @ModelAttribute FavoriteListDetailRequest favoriteListDetailRequest
    ) {
        return ApiResponse.<FavoriteDetailResponse>builder()
                .code("200")
                .status("success")
                .message("Fetched folder favorite details successfully")
                .data(favoriteListService.getFavoriteFolderById(false, folderId, favoriteListDetailRequest)) // false = lấy trong folder
                .build();
    }

    @GetMapping("/{folderId}/addable")
    public ApiResponse<FavoriteDetailResponse> getAddableFlashcards(
            @PathVariable Long folderId,
            @ModelAttribute FavoriteListDetailRequest favoriteListDetailRequest
    ) {
        return ApiResponse.<FavoriteDetailResponse>builder()
                .code("200")
                .status("success")
                .message("Fetched flashcards available for adding successfully")
                .data(favoriteListService.getFavoriteFolderById(true, folderId, favoriteListDetailRequest)) // true = lấy ngoài folder
                .build();
    }


    @GetMapping
    public ApiResponse<Page<FolderFavoriteResponse>> getAllFolders(@ModelAttribute FavoriteListRequest favoriteListRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return ApiResponse.<Page<FolderFavoriteResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched folders successfully")
                .data(favoriteListService.getAllFavoriteLists(username, favoriteListRequest))
                .build();
    }

    @PostMapping
    public ApiResponse<Void> addFavoriteList(@Valid @RequestBody AddFavoriteFolderRequest request) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) auth.getPrincipal();
        favoriteListService.createFavoriteFolder(user, request);
        return ApiResponse.<Void>builder()
                .code("201")
                .status("success")
                .message("Created folder successfully")
                .build();
    }

    @PostMapping("/copy")
    public ApiResponse<Void> copyFavoriteList(@Valid @RequestBody Long folderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        favoriteListService.copyFavoriteFolder(user, folderId);

        return ApiResponse.<Void>builder()
                .code("201")
                .status("success")
                .message("Copied folder successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateFavoriteList(@PathVariable Long id,
                                               @RequestBody AddFavoriteFolderRequest request) {
        favoriteListService.updateFavoriteFolder(id, request);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Updated folder successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFavoriteList(@PathVariable Long id) {
        favoriteListService.deleteFavoriteFolder(id);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Deleted folder successfully")
                .build();
    }


}
