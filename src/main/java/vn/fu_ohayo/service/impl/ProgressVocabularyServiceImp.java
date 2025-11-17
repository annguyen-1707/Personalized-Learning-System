package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ProgressUpdateRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressMapper;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.ProgressVocabularyRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.ProgressVocabularyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ProgressVocabularyServiceImp implements ProgressVocabularyService {

    private  final ProgressVocabularyRepository progressVocabularyRepository;
    private  final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;
    private final SubjectMapper subjectMapper;
    private final ProgressMapper progressMapper;
    private final VocabularyRepository vocabularyRepository;

    public ProgressVocabularyServiceImp(ProgressVocabularyRepository progressVocabularyRepository, UserRepository userRepository,
                                        ProgressSubjectRepository progressSubjectRepository,
                                        SubjectMapper subjectMapper, ProgressMapper progressMapper,
                                        VocabularyRepository vocabularyRepository) {
        this.progressVocabularyRepository = progressVocabularyRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
        this.subjectMapper = subjectMapper;
        this.progressMapper = progressMapper;
        this.vocabularyRepository = vocabularyRepository;
    }
    // lấy ds các vocabulary của tất cả subject đang học của user
    private List<Vocabulary> getVocabulariesOfSubjectsInProgress(User user) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatusIn(user, statuses);
        return progressSubjects.stream()
                .map(ProgressSubject::getSubject)
                .map(Subject::getLessons)
                .flatMap(List::stream)
                .map(Lesson::getVocabularies)
                .flatMap(List::stream)
                .toList();
    }
    // lấy ds các vocabulary của subject đang học của user
    private List<Vocabulary> getVocabulariesEachSubjectInProgress(User user, Subject subject) {
//        ProgressSubject progressSubjects = progressSubjectRepository
//                .findBySubjectAndUserAndProgressStatus(subject,user, ProgressStatus.IN_PROGRESS);
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        ProgressSubject progressSubjects = progressSubjectRepository
                .findBySubjectAndUserAndProgressStatusIn(subject, user, statuses);
        return progressSubjects.getSubject().getLessons()
                .stream()
                .map(Lesson::getVocabularies)
                .flatMap(List::stream)
                .toList();
    }
    // lấy ds số vocabulary đã học theo từng subject của user
    private List<CountLearnBySubjectResponse> getListCountLearnBySubject(User user) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatusIn(user, statuses);
        List<Subject> subjects = progressSubjects.stream()
                .map(ProgressSubject::getSubject)
                .toList();
        return subjects.stream()
                .map(subject -> {
                    List<Vocabulary> vocabularies = getVocabulariesEachSubjectInProgress(user, subject);
                    int countLearn = progressVocabularyRepository
                            .countByUserAndProgressStatusAndVocabularyIn(user, ProgressStatus.COMPLETED, vocabularies);
                    int countAll = vocabularies.size();
                    return CountLearnBySubjectResponse.builder()
                            .subject(subject.getSubjectName())
                            .subjectId(subject.getSubjectId())
                            .countLearn(countLearn)
                            .countAll(countAll)
                            .build();
                })
                .toList();
    }
    // lấy ds vocabulary đã học gần đây của user
    private List<RecentlyLearnVocabularyResponse> getRecentlyLearnWordsByUserId(User user, int size) {
        List<Vocabulary> vocabularies = getVocabulariesOfSubjectsInProgress(user);
        List<ProgressVocabulary> progressVocabularies = progressVocabularyRepository
                .findAllByUserAndTopOnReviewAndVocabularyIn(user, vocabularies, size);
       return progressVocabularies.stream().map(progressMapper::toRecentlyLearnVocabularyResponse).toList();
    }
    @Override
    public int countVocabularyLearnSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Vocabulary> vocabularies = getVocabulariesOfSubjectsInProgress(user);
        return progressVocabularyRepository
                .countByUserAndProgressStatusAndVocabularyIn(user, ProgressStatus.COMPLETED, vocabularies);
    }

    @Override
    public int countAllVocabularySubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Vocabulary> vocabularies = getVocabulariesOfSubjectsInProgress(user);
        return vocabularies.size();
    }

   @Override
    public ProgressVocabularyResponse getProgressEachSubjectByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorEnum.USER_NOT_FOUND));
        return ProgressVocabularyResponse.builder()
                .countLearnBySubjectResponses(getListCountLearnBySubject(user))
                .recentlyLearnVocabularyRespons(getRecentlyLearnWordsByUserId(user, 5))
                .build();
   }

    @Override
    public void updateProgress(int vocabularyId, ProgressUpdateRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND));

        ProgressVocabulary progressVocabulary = progressVocabularyRepository
                .findByUserAndVocabulary(user, vocabulary);

        if (progressVocabulary == null) {
            progressVocabulary = new ProgressVocabulary();
            progressVocabulary.setUser(user);
            progressVocabulary.setVocabulary(vocabulary);
        }
        progressVocabulary.setProgressStatus(request.getStatus().equals("MASTERED") ?
                ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS);
        progressVocabulary.setReviewedAt(new Date());

        progressVocabularyRepository.save(progressVocabulary);
    }

    @Override
    public List<ProgressVocabularyFlashCardResponse> getKnownProgressVocabularies(String email, int lessonId) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        List<Vocabulary> vocabularies = vocabularyRepository.findAllByLessonId(lessonId);
        List<ProgressVocabulary> progressVocabularies = progressVocabularyRepository.findAllByUserAndProgressStatusAndVocabularyIn(user, ProgressStatus.COMPLETED, vocabularies);
        return progressVocabularies.stream().map(
                p -> {
                    ProgressVocabularyFlashCardResponse progressVocabularyFlashCardResponse = new ProgressVocabularyFlashCardResponse();
                    progressVocabularyFlashCardResponse.setVocabularyId(p.getVocabulary().getVocabularyId());
                    progressVocabularyFlashCardResponse.setProgressStatus(p.getProgressStatus());
                    return progressVocabularyFlashCardResponse;
                }
        ).toList();
    }


}
