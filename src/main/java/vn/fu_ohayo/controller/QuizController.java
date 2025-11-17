package vn.fu_ohayo.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.dto.response.QuizGrammarResponse;
import vn.fu_ohayo.dto.response.QuizVocabularyResponse;
import vn.fu_ohayo.entity.FavoriteList;
import vn.fu_ohayo.entity.FavoriteListGrammar;
import vn.fu_ohayo.entity.FavoriteListVocabulary;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.GrammarMapper;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.FavoriteListRepository;
import vn.fu_ohayo.repository.FavoriteVocabularyRepository;
import vn.fu_ohayo.service.QuizService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class QuizController {

    GrammarMapper grammarMapper;
    VocabularyMapper vocabularyMapper;
    QuizService quizService;
    FavoriteListRepository favoriteListRepository;

    @GetMapping("/vocabulary-list")
    public ResponseEntity<Set<QuizVocabularyResponse>> getAllVocabularyList(@RequestParam("id") Long id) {
        quizService.genVocabQuestion(id);
       Set<FavoriteListVocabulary>  favoriteListVocabularies= favoriteListRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)).
                getFavoriteListVocabularies();
       
       Set<QuizVocabularyResponse> list = favoriteListVocabularies
                .stream()
                .map(FavoriteListVocabulary::getVocabulary)
                .map(vocabularyMapper::toQuizResponse)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/grammar-list")
    public ResponseEntity<Set<QuizGrammarResponse>> getAllGrammarList(@RequestParam("id") Long id) {
        quizService.genGrammarQuestion(id);
        Set<FavoriteListGrammar>  favoriteListVocabularies= favoriteListRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)).
                getFavoriteListGrammars();

        Set<QuizGrammarResponse> list = favoriteListVocabularies
                .stream()
                .map(FavoriteListGrammar::getGrammar)
                .map(grammarMapper::toQuizGrammar)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(list);
    }

}
