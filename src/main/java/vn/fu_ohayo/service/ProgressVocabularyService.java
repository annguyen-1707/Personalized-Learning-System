package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.ProgressUpdateRequest;
import vn.fu_ohayo.dto.response.ProgressVocabularyFlashCardResponse;
import vn.fu_ohayo.dto.response.ProgressVocabularyResponse;

import java.util.List;

public interface ProgressVocabularyService {
    int countVocabularyLearnSubjectInProgressByUserId(long userId);
    int countAllVocabularySubjectInProgressByUserId(long userId);
    ProgressVocabularyResponse getProgressEachSubjectByUserId(long userId);

    void updateProgress(int vocabularyId, ProgressUpdateRequest request, String email);

    List<ProgressVocabularyFlashCardResponse> getKnownProgressVocabularies(String email, int lessonId);
}