package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.ProgressUpdateRequest;
import vn.fu_ohayo.dto.response.ProgressGrammarResponse;
import vn.fu_ohayo.dto.response.ProgressGrammarsFlashCardResponse;
import vn.fu_ohayo.dto.response.ProgressVocabularyFlashCardResponse;

import java.util.List;

public interface ProgressGrammarService {
    int countGrammarLearnSubjectInProgressByUserId(long userId);
    int countAllGrammarSubjectInProgressByUserId(long userId);
    ProgressGrammarResponse getProgressEachSubjectByUserId(long userId);

    void updateProgress(int grammarId, ProgressUpdateRequest request, String email);

    List<ProgressGrammarsFlashCardResponse> getKnownProgressGrammars(String email, int lessonId);
}
