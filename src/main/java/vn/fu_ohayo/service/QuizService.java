package vn.fu_ohayo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import vn.fu_ohayo.entity.FavoriteList;
import vn.fu_ohayo.entity.QuizQuestion;
import vn.fu_ohayo.repository.FavoriteListRepository;
import vn.fu_ohayo.repository.FavoriteVocabularyRepository;
import vn.fu_ohayo.repository.QuizRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service

public class QuizService {
    final QuizRepository quizRepository;
    final RestTemplate restTemplate;
    final FavoriteVocabularyRepository favoriteVocabularyRepository;
    final FavoriteListRepository list;

    public void genVocabQuestion(Long favoriteVocabularyId) {
        FavoriteList favoriteList = list.findById(favoriteVocabularyId).orElseThrow(() -> new RuntimeException("Favorite list not found"+ favoriteVocabularyId));
        favoriteList.getFavoriteListVocabularies().forEach(Vocabulary -> {
            if (Vocabulary.getVocabulary().getQuizQuestion() == null) {
                quizRepository.save(QuizQuestion.builder()
                        .vocabulary(Vocabulary.getVocabulary())
                        .question(generateQuiz(Vocabulary.getVocabulary().getKanji() + " (" + Vocabulary.getVocabulary().getKana() + ")", "vocabulary"))
                        .build());
            }
        });
    }

    public void genGrammarQuestion(Long favoriteGrammarId) {
        FavoriteList favoriteList = list.findById(favoriteGrammarId).orElseThrow(() -> new RuntimeException("Favorite list not found"+ favoriteGrammarId));
        favoriteList.getFavoriteListGrammars().forEach(grammar -> {
            if (grammar.getGrammar().getQuizQuestion() == null) {
                quizRepository.save(QuizQuestion.builder()
                        .grammar(grammar.getGrammar())
                        .question(generateQuiz(grammar.getGrammar().getTitleJp() + " (structure :" + grammar.getGrammar().getStructure() + ")", "grammar"))
                        .build());
            }
        });
    }
    public String generateQuiz(String word, String type) {
        String API_KEY = "AIzaSyBUILxmrrbIGNiCcLZaN6RTYom3L9mW0F0";
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String text = "I will give you a " + type + ". Please generate one multiple-choice question where the word I give is the correct answer, but do not include that word in the question itself. Only return the question sentence, no answer options or explanations. Vocabulary word: " + word;

        Map<String, Object> part = Map.of("text", text);
        Map<String, Object> message = Map.of("parts", List.of(part));

        Map<String, Object> generationConfig = Map.of(
                "temperature", 0.7,
                "topK", 32,
                "topP", 1,
                "maxOutputTokens", 1024
        );

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(message));
        body.put("generationConfig", generationConfig);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            return root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();

        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new RuntimeException("Gemini AI đang quá tải, vui lòng thử lại sau.");
            } else {
                throw new RuntimeException("Lỗi kết nối AI: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo câu hỏi từ Gemini");
        }
}
}
