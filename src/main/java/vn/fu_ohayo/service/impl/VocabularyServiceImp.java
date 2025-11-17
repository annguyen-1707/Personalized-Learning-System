package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.VocabularyRequest;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.VocabularyService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VocabularyServiceImp implements VocabularyService {
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyMapper vocabularyMapper;
    private final LessonRepository lessonRepository;

    public VocabularyServiceImp(VocabularyRepository vocabularyRepository, VocabularyMapper vocabularyMapper, LessonRepository lessonRepository) {
        this.vocabularyRepository = vocabularyRepository;
        this.vocabularyMapper = vocabularyMapper;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<VocabularyResponse> getVocabularysByFavoriteVocabularyId(int id) {
        return vocabularyRepository.findAllByFavoriteVocabularyId(id).stream()
                .map(vocabularyMapper::toVocabularyResponse)
                .toList();
    }

    @Override
    public Page<VocabularyResponse> getAllVocabularies(int lessonId, int page, int size) {
        Page<Vocabulary> vocabularyPage = vocabularyRepository.findAllNotInLesson(lessonId, PageRequest.of(page, size));
        return  vocabularyPage.map(vocabularyMapper::toVocabularyResponse);
    }

    @Override
    public void handleSaveVocabulary(int lessonId, int vocabularyId) {
        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId).orElseThrow(
                () -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND)
        );

        if(lessonRepository.existsVocabularyInLesson(vocabularyId,lessonId)) {
            throw new AppException(ErrorEnum.VOCABULARY_EXISTS);
        }
        List<Vocabulary> vocabularies = lesson.getVocabularies();
        vocabularies.add(vocabulary);
        lesson.setVocabularies(vocabularies);
        lessonRepository.save(lesson);

        Set<Lesson> lessons = vocabulary.getLessons();
        lessons.add(lesson);
        vocabulary.setLessons(lessons);
        vocabularyRepository.save(vocabulary);
    }

    @Override
    public VocabularyResponse updatePutVocabulary(int vocabularyId, VocabularyRequest vocabularyRequest) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId).orElseThrow(
                () -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND)
        );
        if(vocabularyRepository.existsDuplicateVocabularyExceptId(vocabularyRequest.getKanji(),
                    vocabularyRequest.getMeaning(),
                    vocabularyId)){
                throw new AppException(ErrorEnum.VOCABULARY_EXISTS);
        }
        if(vocabularyRequest.getKanji() != null) {
            vocabulary.setKanji(vocabularyRequest.getKanji());
        }
        if(vocabularyRequest.getKana() != null) {
            vocabulary.setKana(vocabularyRequest.getKana());
        }
        if(vocabularyRequest.getRomaji() != null) {
            vocabulary.setRomaji(vocabularyRequest.getRomaji());
        }
        if(vocabularyRequest.getMeaning() != null) {
            vocabulary.setMeaning(vocabularyRequest.getMeaning());
        }
        if(vocabularyRequest.getDescription() != null) {
            vocabulary.setDescription(vocabularyRequest.getDescription());
        }
        if(vocabularyRequest.getExample() != null) {
            vocabulary.setExample(vocabularyRequest.getExample());
        }
        if(vocabularyRequest.getPartOfSpeech() != null) {
            vocabulary.setPartOfSpeech(vocabularyRequest.getPartOfSpeech());
        }
        if(vocabularyRequest.getJlptLevel() != null) {
            vocabulary.setJlptLevel(vocabularyRequest.getJlptLevel());
        }
        return vocabularyMapper.toVocabularyResponse(vocabularyRepository.save(vocabulary));
    }

    @Override
    public void deleteVocabularyById(int vocabularyId) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId).orElseThrow(
                () -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND)
        );
        vocabulary.setDeleted(true);
        vocabularyRepository.save(vocabulary);
    }

    @Override
    public Page<VocabularyResponse> getVocabularyPage(int page, int size, int lessonId) {

        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        return vocabularyRepository.findAllByLessonId( lesson.getLessonId(), PageRequest.of(page, size))
                .map(vocabularyMapper::toVocabularyResponse);
    }

    @Override
    public Page<VocabularyResponse> getAllVocabulariesPage(int page, int size) {
        return vocabularyRepository.findAllByDeleted(false,PageRequest.of(page, size))
                .map(vocabularyMapper::toVocabularyResponse);
    }

    @Override
    public VocabularyResponse handleSaveVocabulary(VocabularyRequest vocabularyRequest) {
        Vocabulary existingVocabulary = vocabularyRepository.findAllByKanjiAndKanaAndMeaning(
                vocabularyRequest.getKanji(),
                vocabularyRequest.getKana(),
                vocabularyRequest.getMeaning()
        );
        if(existingVocabulary != null) {
            if(existingVocabulary.getDeleted() == false) {
                throw new AppException(ErrorEnum.VOCABULARY_EXISTS);
            } else{
                updatePutVocabulary(existingVocabulary.getVocabularyId(), vocabularyRequest);
                existingVocabulary.setDeleted(false);
                return vocabularyMapper.toVocabularyResponse(vocabularyRepository.save(existingVocabulary));
            }
        }
        Vocabulary vocabulary = vocabularyMapper.toVocabulary(vocabularyRequest);
        vocabulary.setDeleted(false);
        vocabulary.setIsSystem(true);
        return vocabularyMapper.toVocabularyResponse(vocabularyRepository.save(vocabulary));
    }

    @Override
    public void deleteVocabularyFromLesson(int id, int lessonId) {
        vocabularyRepository.removeVocabInLessonId(id, lessonId);
    }
}
