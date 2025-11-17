package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.LessonPatchRequest;
import vn.fu_ohayo.dto.request.LessonRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.dto.response.LessonResponse;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.GrammarMapper;
import vn.fu_ohayo.mapper.LessonMapper;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.LessonService;

import java.util.List;

@Service
public class LessonServiceImp implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final SubjectRepository subjectRepository;
    private final VocabularyMapper vocabularyMapper;
    private final GrammarMapper grammarMapper;

    public LessonServiceImp(LessonRepository lessonRepository, LessonMapper lessonMapper,
                            SubjectRepository subjectRepository,
                            VocabularyMapper vocabularyMapper,
                            GrammarMapper grammarMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.subjectRepository = subjectRepository;
        this.vocabularyMapper = vocabularyMapper;
        this.grammarMapper = grammarMapper;
    }

    @Override
    public LessonResponse createLesson(LessonRequest lessonRequest) {
        Subject subject = subjectRepository.findById(lessonRequest.getSubjectId()).orElseThrow(
                () -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND)
        );
        if (lessonRepository.existsByName(lessonRequest.getName())) {
            throw new AppException(ErrorEnum.LESSON_NAME_EXIST);
        }
        Lesson lesson = Lesson.builder()
                .name(lessonRequest.getName())
                .description(lessonRequest.getDescription())
                .subject(subject)
                .videoUrl(lessonRequest.getVideoUrl())
                .status(LessonStatus.DRAFT)
                .build();
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }



    @Override
    public Page<LessonResponse> getAllLessons(int subjectId, int page, int pageSize) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND)
        );
        Page<Lesson> lessons = lessonRepository.findAllBySubject(subject,PageRequest.of(page, pageSize));
        return lessons.map(lessonMapper::toLessonResponse);
    }

    @Override
    public LessonResponse getLessonById(int id) {
        return lessonMapper.toLessonResponse(lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        ));
    }

    @Override
    public List<LessonResponse> getLessonBySubjectId(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND)
        );

        return lessonRepository.findAllBySubjectAndStatus(subject, LessonStatus.PUBLIC).stream().map(
                lesson -> {
                    LessonResponse lessonResponse = lessonMapper.toLessonResponse(lesson);
                    List<VocabularyResponse> vocabularyResponses = lesson.getVocabularies()
                            .stream()
                            .map(vocabularyMapper::toVocabularyResponse)
                            .toList();
                    List<GrammarResponse> grammarResponses = lesson.getGrammars()
                            .stream().map(grammarMapper::toGrammarResponse)
                            .toList();
                    lessonResponse.setVocabularies(vocabularyResponses);
                    lessonResponse.setGrammars(grammarResponses);
                    return lessonResponse;
                }
        ).toList();
    }

    @Override
    public LessonResponse acceptLesson(int id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );

        if(lesson.getLessonExercises().isEmpty() || lesson.getVocabularies().isEmpty() || lesson.getGrammars().isEmpty()) {
            throw new AppException(ErrorEnum.LESSON_NOT_COMPLETE);
        }

        if (lesson.getStatus() == LessonStatus.DRAFT) {
            lesson.setStatus(LessonStatus.PUBLIC);
            return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
        }
        return null;
    }

    @Override
    public LessonResponse rejectLesson(int id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        if (lesson.getStatus() == LessonStatus.DRAFT) {
            lesson.setStatus(LessonStatus.REJECTED);
            return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
        }
        return null;
    }

    @Override
    public LessonResponse inactiveLesson(int id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        if (lesson.getStatus() == LessonStatus.PUBLIC) {
            lesson.setStatus(LessonStatus.IN_ACTIVE);
            return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
        }
        return null;
    }

    @Override
    public LessonResponse updateLesson(Integer id, LessonPatchRequest lessonRequest) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        if(lessonRequest.getName() != null) {
            if (lessonRepository.existsByNameAndLessonIdNot(lessonRequest.getName(), id)) {
                throw new AppException(ErrorEnum.LESSON_NAME_EXIST);
            }
            lesson.setName(lessonRequest.getName());
        }
        if(lessonRequest.getDescription() != null) {
            lesson.setDescription(lessonRequest.getDescription());
        }
        if(lessonRequest.getVideoUrl() != null) {
            lesson.setVideoUrl(lessonRequest.getVideoUrl());
        }
        lesson.setStatus(LessonStatus.DRAFT);
        return lessonMapper.toLessonResponse(lessonRepository.save(lesson));
    }

    @Override
    public void deleteLesson(Integer id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorEnum.LESSON_NOT_FOUND)
        );
        if (!lesson.getProgressLessons().isEmpty()) {
            throw new AppException(ErrorEnum.LESSON_IN_USE);
        }
        lesson.setDeleted(true);
        lessonRepository.save(lesson);
    }
}
