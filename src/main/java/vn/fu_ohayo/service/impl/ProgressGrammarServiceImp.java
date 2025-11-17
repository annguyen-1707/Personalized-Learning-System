package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ProgressUpdateRequest;
import vn.fu_ohayo.dto.response.CountLearnBySubjectResponse;
import vn.fu_ohayo.dto.response.ProgressGrammarResponse;
import vn.fu_ohayo.dto.response.ProgressGrammarsFlashCardResponse;
import vn.fu_ohayo.dto.response.RecentlyLearnGrammarResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressMapper;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.ProgressGrammarService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressGrammarServiceImp implements ProgressGrammarService {
    private final ProgressGrammarRepository progressGrammarRepository;
    private final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;
    private final ProgressMapper progressMapper;
    private final GrammarRepository grammarRepository;

    public ProgressGrammarServiceImp(ProgressGrammarRepository progressGrammarRepository,
                                     UserRepository userRepository,
                                     ProgressSubjectRepository progressSubjectRepository,
                                     ProgressMapper progressMapper,
                                     GrammarRepository grammarRepository) {
        this.progressGrammarRepository = progressGrammarRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
        this.progressMapper = progressMapper;
        this.grammarRepository = grammarRepository;
    }

    private List<Grammar> getGrammarsOfSubjectsInProgress(User user) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatusIn(user, statuses);
        return progressSubjects.stream()
                .map(ProgressSubject::getSubject)
                .map(Subject::getLessons)
                .flatMap(List::stream)
                .map(Lesson::getGrammars)
                .flatMap(List::stream)
                .toList();
    }

    // lấy ds các Grammar của subject đang học của user
    private List<Grammar> getGrammarsEachSubjectInProgress(User user, Subject subject) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        ProgressSubject progressSubjects = progressSubjectRepository
                .findBySubjectAndUserAndProgressStatusIn(subject, user, statuses);
        return progressSubjects.getSubject().getLessons()
                .stream()
                .map(Lesson::getGrammars)
                .flatMap(List::stream)
                .toList();
    }

    // lấy ds số Grammar đã học theo từng subject của user
    private List<CountLearnBySubjectResponse> getListCountLearnBySubject(User user) {
        List<ProgressStatus> statuses = Arrays.asList(ProgressStatus.IN_PROGRESS, ProgressStatus.COMPLETED);
        List<ProgressSubject> progressSubjects = progressSubjectRepository
                .findAllByUserAndProgressStatusIn(user, statuses);
        List<Subject> subjects = progressSubjects.stream()
                .map(ProgressSubject::getSubject)
                .toList();
        return subjects.stream()
                .map(subject -> {
                    List<Grammar> Grammars = getGrammarsEachSubjectInProgress(user, subject);
                    int countLearn = progressGrammarRepository
                            .countByUserAndProgressStatusAndGrammarIn(user, ProgressStatus.COMPLETED, Grammars);
                    int countAll = Grammars.size();
                    return CountLearnBySubjectResponse.builder()
                            .subject(subject.getSubjectName())
                            .subjectId(subject.getSubjectId())
                            .countLearn(countLearn)
                            .countAll(countAll)
                            .build();
                })
                .toList();
    }

    // lấy ds Grammar đã học gần đây của user
    private List<RecentlyLearnGrammarResponse> getRecentlyLearnWordsByUserId(User user, int size) {
        List<Grammar> Grammars = getGrammarsOfSubjectsInProgress(user);
        List<ProgressGrammar> progressGrammars = progressGrammarRepository
                .findAllByUserAndTopOnReviewAndGrammarIn(user, Grammars, size);
        return progressGrammars.stream().map(progressMapper::toRecentlyLearnGrammarResponse).toList();
    }

    @Override
    public int countGrammarLearnSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Grammar> grammars = getGrammarsOfSubjectsInProgress(user);

        List<ProgressGrammar> progressGrammars = progressGrammarRepository
                .findAllByUserAndProgressStatusAndGrammarIn(user, ProgressStatus.COMPLETED, grammars);
        return progressGrammars.size();
    }

    @Override
    public int countAllGrammarSubjectInProgressByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Grammar> grammars = getGrammarsOfSubjectsInProgress(user);
        return grammars.size();
    }

    @Override
    public ProgressGrammarResponse getProgressEachSubjectByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        return ProgressGrammarResponse.builder()
                .countLearnBySubjectResponses(getListCountLearnBySubject(user))
                .recentlyLearnGrammarResponses(getRecentlyLearnWordsByUserId(user, 5))
                .build();
    }

    @Override
    public void updateProgress(int grammarId, ProgressUpdateRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        ProgressGrammar progressGrammar = progressGrammarRepository.findAllByGrammar_GrammarIdAndUser(grammarId, user);
        if (progressGrammar != null) {
            progressGrammar.setProgressStatus(request.getStatus().equals("MASTERED") ?
                    ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS);
            progressGrammar.setReviewedAt(new Date());
            progressGrammarRepository.save(progressGrammar);
        } else {
            Grammar grammar = grammarRepository.findById(grammarId)
                    .orElseThrow(() -> new AppException(ErrorEnum.GRAMMAR_NOT_FOUND));
            ProgressGrammar newProgressGrammar = ProgressGrammar.builder()
                    .user(user)
                    .grammar(grammar)
                    .progressStatus(request.getStatus().equals("MASTERED") ?
                            ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS)
                    .reviewedAt(new Date())
                    .build();
            progressGrammarRepository.save(newProgressGrammar);
        }


    }

    @Override
    public List<ProgressGrammarsFlashCardResponse> getKnownProgressGrammars(String email, int lessonId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        List<Grammar> grammars = grammarRepository.findAllByLessonId(lessonId);
        List<ProgressGrammar> progressGrammars = progressGrammarRepository
                .findAllByUserAndProgressStatusAndGrammarIn(user, ProgressStatus.COMPLETED, grammars);
       return progressGrammars.stream().map(
               pg -> ProgressGrammarsFlashCardResponse
                       .builder()
                       .grammarId(pg.getGrammar().getGrammarId())
                       .progressStatus(pg.getProgressStatus())
                       .build()
       ).toList();

    }
}
