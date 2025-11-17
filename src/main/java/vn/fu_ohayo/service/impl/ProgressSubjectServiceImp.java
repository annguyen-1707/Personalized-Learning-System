package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressSubjectMapper;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.ProgressLessonRepository;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ProgressSubjectService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressSubjectServiceImp implements ProgressSubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ProgressSubjectRepository progressSubjectRepository;
    private final ProgressSubjectMapper progressSubjectMapper;
    private final ProgressLessonRepository progressLessonRepository;


    public ProgressSubjectServiceImp(SubjectRepository subjectRepository,
                                     UserRepository userRepository,
                                     ProgressSubjectRepository progressSubjectRepository,
                                     ProgressLessonRepository progressLessonRepository,
                                     ProgressSubjectMapper progressSubjectMapper) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.progressSubjectRepository = progressSubjectRepository;
        this.progressLessonRepository = progressLessonRepository;
        this.progressSubjectMapper = progressSubjectMapper;
    }
    @Override
    public void enrollCourse(int courseId, String email) {
        Subject subject = subjectRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        ProgressSubject existingProgress = progressSubjectRepository.findProgressSubjectBySubjectAndUser(subject, user);

        if (existingProgress != null) {
                existingProgress.setViewedAt(new Date());
            progressSubjectRepository.save(existingProgress);
                return;
        }

        ProgressSubject progressSubject = ProgressSubject.builder()
                .subject(subject)
                .startDate(new Date())
                .viewedAt(new Date())
                .user(user)
                .progressStatus(ProgressStatus.IN_PROGRESS)
                .build();
        progressSubjectRepository.save(progressSubject);
    }

    @Override
    public ProgressSubjectResponse getProgressSubject(String email, int subjectId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));

        ProgressSubject progressSubject = progressSubjectRepository.findProgressSubjectBySubjectAndUser(subject, user);
        if (progressSubject == null) {
            throw new AppException(ErrorEnum.PROGRESS_NOT_FOUND);
        }
        return progressSubjectMapper.toProgressSubjectResponse(progressSubject);

    }

    @Override
    public ProgressSubjectResponse markSubjectComplete(String username, int subjectId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND)
        );
        ProgressSubject progressSubject = progressSubjectRepository.findProgressSubjectBySubjectAndUser(subject, user);
        if (progressSubject != null) {
            markLessonComplete(subject, user);
            progressSubject.setViewedAt(new Date());
            progressSubject.setProgressStatus(ProgressStatus.COMPLETED);
            progressSubject.setEndDate(new Date());
            return progressSubjectMapper.toProgressSubjectResponse(
                    progressSubjectRepository.save(progressSubject)
            );
        }
        return null;
    }

    public void markLessonComplete(Subject subject, User user) {
        List<Lesson> lessons = subject.getLessons();

        List<ProgressLesson> progressLessons = progressLessonRepository.findAllByUserAndLessonIn(user, lessons);
        if (progressLessons.isEmpty()) {
            throw new AppException(ErrorEnum.PROGRESS_NOT_FOUND);
        }
        for (ProgressLesson progressLesson : progressLessons) {
            progressLesson.setViewedAt(new Date());
            progressLesson.setStatus(ProgressStatus.COMPLETED);
            progressLesson.setEndDate(new Date());
            progressLessonRepository.save(progressLesson);
        }
    }
}
