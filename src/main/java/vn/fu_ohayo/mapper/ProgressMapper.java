package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.ProgressVocabularyFlashCardResponse;
import vn.fu_ohayo.dto.response.RecentlyLearnExerciseResponse;
import vn.fu_ohayo.dto.response.RecentlyLearnGrammarResponse;
import vn.fu_ohayo.dto.response.RecentlyLearnVocabularyResponse;
import vn.fu_ohayo.entity.ExerciseResult;
import vn.fu_ohayo.entity.ProgressGrammar;
import vn.fu_ohayo.entity.ProgressVocabulary;

@Mapper(componentModel = "spring")
public interface ProgressMapper {
    RecentlyLearnVocabularyResponse toRecentlyLearnVocabularyResponse(ProgressVocabulary progressVocabulary);
    RecentlyLearnGrammarResponse toRecentlyLearnGrammarResponse(ProgressGrammar progressGrammar);
    RecentlyLearnExerciseResponse toRecentlyLearnExerciseResponse(ExerciseResult exerciseResult);

    ProgressVocabularyFlashCardResponse toProgressVocabularyFlashCardResponse(ProgressVocabulary progressVocabulary);

}
