package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.fu_ohayo.dto.request.AddFavoriteFolderRequest;
import vn.fu_ohayo.dto.request.FavoriteListDetailRequest;
import vn.fu_ohayo.dto.request.FavoriteListRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.FlashcardEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;
import vn.fu_ohayo.mapper.GrammarMapper;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.FavoriteListService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteListServiceImp implements FavoriteListService {

    private final FavoriteListRepository favoriteListRepository;
    private final FavoriteListVocabularyRepository favoriteListVocabularyRepository;
    private final FavoriteListGrammarRepository favoriteListGrammarRepository;
    private final VocabularyRepository vocabularyRepository;
    private final GrammarRepository grammarRepository;
    private final VocabularyMapper vocabularyMapper;
    private final GrammarMapper grammarMapper;

    @Override
    @Transactional
    public void addItemToFavoriteList(long favoriteListId, String type, int itemId) {
        FavoriteList favoriteList = favoriteListRepository.findById(favoriteListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite list not found with ID: " + favoriteListId));

        if ("vocabulary".equalsIgnoreCase(type)) {
            Vocabulary vocabulary = vocabularyRepository.findById(itemId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found with ID: " + itemId));

            // Check if vocabulary already exists in the favorite list
            if (favoriteListVocabularyRepository.findByFavoriteListIdAndVocabularyId(favoriteListId, itemId).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Vocabulary already exists in this favorite list.");
            }

            FavoriteListVocabulary favoriteListVocabulary = FavoriteListVocabulary.builder()
                    .favoriteListId(favoriteList.getFavoriteId())
                    .vocabularyId(vocabulary.getVocabularyId())
                    .favoriteList(favoriteList)
                    .vocabulary(vocabulary)
                    .status(FlashcardEnum.NOT_LEARNED) // Default status for new items
                    .lastReviewedAt(new Date())
                    .build();
            favoriteListVocabularyRepository.save(favoriteListVocabulary);

        } else if ("grammar".equalsIgnoreCase(type)) {
            Grammar grammar = grammarRepository.findById(itemId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grammar not found with ID: " + itemId));

            // Check if grammar already exists in the favorite list
            if (favoriteListGrammarRepository.findByFavoriteListIdAndGrammarId(favoriteListId, itemId).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Grammar already exists in this favorite list.");
            }

            FavoriteListGrammar favoriteListGrammar = FavoriteListGrammar.builder()
                    .favoriteListId(favoriteList.getFavoriteId())
                    .grammarId(grammar.getGrammarId())
                    .favoriteList(favoriteList)
                    .grammar(grammar)
                    .status(FlashcardEnum.NOT_LEARNED) // Default status for new items
                    .lastReviewedAt(new Date())
                    .build();
            favoriteListGrammarRepository.save(favoriteListGrammar);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid content type: " + type + ". Must be 'vocabulary' or 'grammar'.");
        }
    }

    @Override
    public List<FlashcardResponse> getAllFlashcards(String type, long favoriteListId) {
        // 1. Kiểm tra folder tồn tại
        favoriteListRepository.findById(favoriteListId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite list not found: " + favoriteListId));

        List<FlashcardResponse> result = new ArrayList<>();

        if ("vocabulary".equalsIgnoreCase(type)) {
            // 2a. Lấy Vocabulary
            List<FavoriteListVocabulary> vocabs =
                    favoriteListVocabularyRepository.findByFavoriteListIdWithVocabulary(favoriteListId);
            for (FavoriteListVocabulary fv : vocabs) {
                var v = fv.getVocabulary();
                result.add(FlashcardResponse.builder()
                        .id((long) v.getVocabularyId())
                        .frontText(v.getKanji())
                        .subText(v.getKana())
                        .translation(v.getMeaning())
                        .notes(v.getDescription())
                        .jlptLevel(v.getJlptLevel().name())
                        .romaji(v.getRomaji())
                        .partOfSpeech(v.getPartOfSpeech().name())
                        .status(fv.getStatus())
                        .build());
            }

        } else if ("grammar".equalsIgnoreCase(type)) {
            // 2b. Lấy Grammar
            List<FavoriteListGrammar> grams =
                    favoriteListGrammarRepository.findByFavoriteListIdWithGrammar(favoriteListId);
            for (FavoriteListGrammar fg : grams) {
                var g = fg.getGrammar();
                result.add(FlashcardResponse.builder()
                        .id((long) g.getGrammarId())
                        .frontText(g.getTitleJp())
                        .subText(g.getStructure())
                        .translation(g.getMeaning())
                        .notes(g.getUsage())
                        .jlptLevel(g.getJlptLevel().name())
                        .status(fg.getStatus())
                        .build());
            }

        } else {
            throw new IllegalArgumentException("Unknown flashcard type: " + type);
        }

        return result;
    }

    @Override
    @Transactional
    public void updateFlashCardStatus(
            String type,
            long favoriteListId,
            int flashcardId,
            FlashcardEnum status
    ) {
        // 1. Kiểm tra folder tồn tại
        favoriteListRepository.findById(favoriteListId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite list not found: " + favoriteListId));

        // 2. Tuỳ vào type:
        if ("vocabulary".equalsIgnoreCase(type)) {
            FavoriteListVocabulary entity = favoriteListVocabularyRepository
                    .findByFavoriteListIdAndVocabularyId(favoriteListId, flashcardId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Vocabulary flashcard not found in list " + favoriteListId + ": " + flashcardId));

            entity.setStatus(status);
            entity.setLastReviewedAt(new Date());
            favoriteListVocabularyRepository.save(entity);

        } else if ("grammar".equalsIgnoreCase(type)) {
            FavoriteListGrammar entity = favoriteListGrammarRepository
                    .findByFavoriteListIdAndGrammarId(favoriteListId, flashcardId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Grammar flashcard not found in list " + favoriteListId + ": " + flashcardId));

            entity.setStatus(status);
            entity.setLastReviewedAt(new Date());
            favoriteListGrammarRepository.save(entity);

        } else {
            throw new IllegalArgumentException("Unknown flashcard type: " + type);
        }
    }

    @Override
    public FlashCardStatusResponse getFlashCardStatus(int favoriteListId) {
        long countNotLearned = favoriteListVocabularyRepository
                .countByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.NOT_LEARNED)
                + favoriteListGrammarRepository
                .countByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.NOT_LEARNED);
        long countInProgress = favoriteListVocabularyRepository
                .countByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.IN_PROGRESS)
                + favoriteListGrammarRepository
                .countByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.IN_PROGRESS);
        long countMastered = favoriteListVocabularyRepository
                .countByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.MASTERED)
                + favoriteListGrammarRepository
                .countByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.MASTERED);

        List<Integer> vocabMastered = favoriteListVocabularyRepository
                .findVocabularyIdsByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.MASTERED);
        List<Integer> grammarMastered = favoriteListGrammarRepository
                .findGrammarIdsByFavoriteListIdAndStatus(favoriteListId, FlashcardEnum.MASTERED);

        List<Integer> flashcardMasteredIds = new ArrayList<>();
        flashcardMasteredIds.addAll(vocabMastered);
        flashcardMasteredIds.addAll(grammarMastered);

        return FlashCardStatusResponse.builder()
                .countNotLearned((int) countNotLearned)
                .countInProgress((int) countInProgress)
                .countMastered((int) countMastered)
                .flashcardMasteredIds(flashcardMasteredIds)
                .build();
    }

    public void copyFavoriteFolder(User user, Long folderId) {
        FavoriteList original = favoriteListRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!original.isPublic()) {
            throw new RuntimeException("You can only copy public folders.");
        }

        FavoriteList copy = FavoriteList.builder()
                .favoriteListName(original.getFavoriteListName())
                .user(user)
                .isPublic(original.isPublic())
                .isDeleted(original.isDeleted())
                .createdAt(new Date())
                .build();
        favoriteListRepository.save(copy);

        List<FavoriteListVocabulary> vocabCopies = original.getFavoriteListVocabularies().stream()
                .map(origVocab -> FavoriteListVocabulary.builder()
                        .favoriteListId(copy.getFavoriteId())
                        .vocabularyId(origVocab.getVocabularyId())
                        .status(origVocab.getStatus())
                        .lastReviewedAt(origVocab.getLastReviewedAt())
                        .build()
                )
                .collect(Collectors.toList());
        favoriteListVocabularyRepository.saveAll(vocabCopies);

        List<FavoriteListGrammar> grammarCopies = original.getFavoriteListGrammars().stream()
                .map(origGram -> FavoriteListGrammar.builder()
                        .favoriteListId(copy.getFavoriteId())
                        .grammarId(origGram.getGrammarId())
                        .status(origGram.getStatus())
                        .lastReviewedAt(origGram.getLastReviewedAt())
                        .build()
                )
                .collect(Collectors.toList());
        favoriteListGrammarRepository.saveAll(grammarCopies);
    }

    @Override
    public Page<FolderFavoriteResponse> getAllFavoriteLists(String username, FavoriteListRequest request) {
        int page = request.getCurrentPage() != null ? request.getCurrentPage() : 0;
        int size = request.getPageSize() != null ? request.getPageSize() : 21;
        Pageable pageable = PageRequest.of(page, size);
        String keyword = request.getSearchName() != null ? request.getSearchName() : "";
        String type = request.getType(); // NEW: "vocabulary", "grammar", or "all"
        String view = request.getViewType(); // NEW: "mine" or "public"
        Boolean isPublic = request.getIsPublic();

        Page<FavoriteList> pageResult;

        if ("public".equalsIgnoreCase(view)) {
            if ("vocabulary".equalsIgnoreCase(type)) {
                pageResult = favoriteListRepository.findPublicVocabularyFoldersExcludeUser(username, keyword, pageable);
            } else if ("grammar".equalsIgnoreCase(type)) {
                pageResult = favoriteListRepository.findPublicGrammarFoldersExcludeUser(username, keyword, pageable);
            } else {
                pageResult = favoriteListRepository.findPublicFoldersExcludeUser(username, keyword, pageable);
            }
        } else {
            // Mặc định là "mine"
            if (isPublic != null) {
                if ("vocabulary".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findMyVocabularyFoldersWithPublic(username, keyword, isPublic, pageable);
                } else if ("grammar".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findMyGrammarFoldersWithPublic(username, keyword, isPublic, pageable);
                } else {
                    pageResult = favoriteListRepository.findMyAllFoldersWithPublic(username, keyword, isPublic, pageable);
                }
            } else {
                if ("vocabulary".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findVocabularyFolders(username, keyword, pageable);
                } else if ("grammar".equalsIgnoreCase(type)) {
                    pageResult = favoriteListRepository.findGrammarFolders(username, keyword, pageable);
                } else {
                    pageResult = favoriteListRepository.findAllFolders(username, keyword, pageable);
                }
            }
        }

        return pageResult.map(fav -> {
            long vocabCount = favoriteListVocabularyRepository.countByFavoriteListId(fav.getFavoriteId());
            long grammarCount = favoriteListGrammarRepository.countByFavoriteListId(fav.getFavoriteId());

            return FolderFavoriteResponse.builder()
                    .id(fav.getFavoriteId())
                    .name(fav.getFavoriteListName())
                    .isPublic(fav.isPublic())
                    .addedAt(fav.getCreatedAt())
                    .ownerName(fav.getUser().getFullName())
                    .numberOfGrammar((int)grammarCount)
                    .numberOfVocabulary((int)vocabCount)
                    .build();
        });
    }

    @Override
    public void createFavoriteFolder(User user, AddFavoriteFolderRequest request) {
        FavoriteList favoriteList = new FavoriteList();
        favoriteList.setFavoriteListName(request.getName());
        favoriteList.setPublic(request.getIsPublic());
        favoriteList.setUser(user);
        favoriteListRepository.save(favoriteList);
    }

    @Override
    public void updateFavoriteFolder(Long id, AddFavoriteFolderRequest request) {
        FavoriteList folder = favoriteListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        folder.setFavoriteListName(request.getName());
        folder.setPublic(request.getIsPublic());
        favoriteListRepository.save(folder);
    }

    @Override
    public void deleteFavoriteFolder(Long id) {
        FavoriteList folder = favoriteListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        folder.setDeleted(true);
        favoriteListRepository.save(folder);
    }

    @Override
    public FavoriteDetailResponse getFavoriteFolderById(boolean exclude, Long folderId, FavoriteListDetailRequest request) {
        FavoriteList favoriteList = favoriteListRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        String type = request.getType();
        String keyword = request.getSearchName();
        JlptLevel jlpt = request.getJlptLevel();
        PartOfSpeech part = request.getCategory();

        Pageable pageable = PageRequest.of(
                request.getCurrentPage() != null ? request.getCurrentPage() : 0,
                request.getPageSize() != null ? request.getPageSize() : 20
        );

        Page<VocabularyResponse> vocabularyPage = Page.empty();
        Page<GrammarResponse> grammarPage = Page.empty();

        if ("vocabulary".equalsIgnoreCase(type)) {
            if (exclude) {
                vocabularyPage = favoriteListVocabularyRepository.searchVocabulariesNotInFolder(
                        folderId, keyword, part, jlpt, pageable
                ).map(vocabularyMapper::toVocabularyResponse);
            } else {
                vocabularyPage = favoriteListVocabularyRepository.searchVocabulariesByFolderId(
                        folderId, keyword, part, jlpt, pageable
                ).map(vocabularyMapper::toVocabularyResponse);
            }
        }

        if ("grammar".equalsIgnoreCase(type)) {
            if (exclude) {
                grammarPage = favoriteListGrammarRepository.searchGrammarsNotInFolder(
                        folderId, keyword, jlpt, pageable
                ).map(grammarMapper::toGrammarResponse);
            } else {
                grammarPage = favoriteListGrammarRepository.searchGrammarsByFolderId(
                        folderId, keyword, jlpt, pageable
                ).map(grammarMapper::toGrammarResponse);
            }
        }

        return FavoriteDetailResponse.builder()
                .favoriteId(favoriteList.getFavoriteId())
                .favoriteListName(favoriteList.getFavoriteListName())
                .isPublic(favoriteList.isPublic())
                .createdAt(favoriteList.getCreatedAt())
                .ownerName(favoriteList.getUser().getFullName())
                .vocabularyList(vocabularyPage)
                .grammarList(grammarPage)
                .build();
    }

}
