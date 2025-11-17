package vn.fu_ohayo.controller.favoriteList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.FlashCardStatusResponse;
import vn.fu_ohayo.dto.response.FlashcardResponse;
import vn.fu_ohayo.enums.FlashcardEnum;
import vn.fu_ohayo.service.FavoriteListService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FavoriteListService favoriteListService;

    @GetMapping
    public ApiResponse<List<FlashcardResponse>> getAllFlashcards(
            @RequestParam String type,
            @RequestParam int favoriteListId) {

        List<FlashcardResponse> allCards = favoriteListService.getAllFlashcards(type, favoriteListId);
        return ApiResponse.<List<FlashcardResponse>>builder()
                .code("200")
                .status("success")
                .message("Fetched all flashcards successfully")
                .data(allCards)
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<FlashCardStatusResponse> getFlashCardStatus(
            @RequestParam int favoriteListId) {
        FlashCardStatusResponse response = favoriteListService.getFlashCardStatus(favoriteListId);
        return ApiResponse.<FlashCardStatusResponse>builder()
                .code("200")
                .status("success")
                .message("Flashcard status counts fetched successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/status")
    public ApiResponse<Void> updateFlashCardStatus(
            @RequestParam String type, //grammar or vocabulary
            @RequestParam int favoriteListId,
            @RequestParam int flashcardId,
            @RequestParam FlashcardEnum status) {
        favoriteListService.updateFlashCardStatus(type, favoriteListId, flashcardId, status);
        return ApiResponse.<Void>builder()
                .code("200")
                .status("success")
                .message("Flashcard status updated to " + status)
                .build();
    }
}
