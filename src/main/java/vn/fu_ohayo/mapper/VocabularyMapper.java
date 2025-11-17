package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.fu_ohayo.dto.request.VocabularyRequest;
import vn.fu_ohayo.dto.response.QuizVocabularyResponse;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.entity.Vocabulary;

@Mapper(componentModel = "spring")
public interface VocabularyMapper {
    Vocabulary toVocabulary(VocabularyRequest vocabularyRequest);

    @Mapping(source = "quizQuestion", target = "quizQuestion")
    VocabularyResponse toVocabularyResponse(Vocabulary vocabulary);

    @Mapping(source = "quizQuestion.question", target = "quizQuestion")
    QuizVocabularyResponse toQuizResponse(Vocabulary vocabulary);
}
