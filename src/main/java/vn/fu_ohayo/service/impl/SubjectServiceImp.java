package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.SubjectRequest;
import vn.fu_ohayo.dto.response.ProgressSubjectResponse;
import vn.fu_ohayo.dto.response.SubjectResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.ProgressSubject;
import vn.fu_ohayo.entity.Subject;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.LessonStatus;
import vn.fu_ohayo.enums.SubjectStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.SubjectMapper;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.ProgressSubjectRepository;
import vn.fu_ohayo.repository.SubjectRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.SubjectService;

import java.util.List;


@Service
public class SubjectServiceImp implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProgressSubjectRepository progressSubjectRepository;

    public SubjectServiceImp(SubjectRepository subjectRepository, SubjectMapper subjectMapper,
                             LessonRepository lessonRepository,
                             UserRepository userRepository,
                             UserMapper userMapper,
                             ProgressSubjectRepository progressSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.progressSubjectRepository = progressSubjectRepository;
    }

    @Override
    public Page<SubjectResponse> getAllActiveSubjects(int page, int size, String email) {
        return subjectRepository.findAllByStatusAndProgressSubjectsIsEmpty(SubjectStatus.PUBLIC, email, PageRequest.of(page, size))
                .map(subjectMapper::toSubjectResponse)
                .map(s -> {
                    s.setCountUsers(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) > 0 ? progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) : 0);
                    s.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(s.getSubjectId(), LessonStatus.PUBLIC), 0));
                    return s;
                });
    }


    @Override
    public Page<SubjectResponse> getAllSubjectsForAdmin(int page, int size) {
        return subjectRepository.findAllByIsDeleted(false,PageRequest.of(page, size))
                .map(subjectMapper::toSubjectResponse)
                .map(s -> {
                    s.setCountUsers(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) > 0 ? progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) : 0);
                    s.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectId(s.getSubjectId()), 0));
                    return s;
                });
    }

    @Override
    public Page<ProgressSubjectResponse> getAllByUserId(int page, int size, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Page<ProgressSubject> progressSubjects = progressSubjectRepository.findAllByUserAndSubject_Status(user, SubjectStatus.PUBLIC, PageRequest.of(page, size));

        if (progressSubjects.hasContent()) {
            return progressSubjects.map(ps -> {
                ProgressSubjectResponse response = new ProgressSubjectResponse();
                response.setProgressId(ps.getProgressId());
                response.setUser(userMapper.toUserResponse(ps.getUser()));
                response.setSubject(subjectMapper.toSubjectResponse(ps.getSubject()));
                response.setProgressStatus(ps.getProgressStatus());
                response.getSubject().setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(ps.getSubject().getSubjectId(), LessonStatus.PUBLIC), 0));
                response.getSubject().setCountUsers(
                        Math.max(progressSubjectRepository.countUserBySubject_SubjectId(ps.getSubject().getSubjectId()), 0));
                return response;
            });
        }
        return Page.empty();
    }

    @Override
    public Page<SubjectResponse> getAllPublicSubjects(int page, int size) {
        return subjectRepository.findAllByStatusAndIsDeleted(SubjectStatus.PUBLIC, false, PageRequest.of(page, size))
                .map(subjectMapper::toSubjectResponse)
                .map(s -> {
                    s.setCountUsers(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) > 0 ? progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()) : 0);
                    s.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(s.getSubjectId(), LessonStatus.PUBLIC), 0));
                    return s;
                });
    }

    @Override
    public List<SubjectResponse> getAllListActiveSubjectsByStatus() {
        return subjectRepository.findAllByStatus(SubjectStatus.PUBLIC).stream().map(subjectMapper::toSubjectResponse).toList() ;
    }

    @Override
    public List<SubjectResponse> getAllListSubjects() {
        return subjectRepository.findAll().stream().map(subjectMapper::toSubjectResponse).toList() ;
    }

    @Override
    public SubjectResponse acceptSubject(int id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
        if(subject.getLessons().isEmpty()) {
            throw new AppException(ErrorEnum.SUBJECT_CONTENT_EMPTY);
        }
        int count  = lessonRepository.countAllBySubject_SubjectIdAndStatus(subject.getSubjectId(), LessonStatus.PUBLIC);
        if(count == 0) {
            throw new AppException(ErrorEnum.SUBJECT_CONTENT_EMPTY);
        }
        if (subject.getStatus() == SubjectStatus.DRAFT) {
            subject.setStatus(SubjectStatus.PUBLIC);
            Subject updatedSubject = subjectRepository.save(subject);
            return subjectMapper.toSubjectResponse(updatedSubject);
        }
        return null;
    }

    @Override
    public SubjectResponse rejectSubject(int id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
        if (subject.getStatus() == SubjectStatus.DRAFT) {
            subject.setStatus(SubjectStatus.REJECTED);
            Subject updatedSubject = subjectRepository.save(subject);
            return subjectMapper.toSubjectResponse(updatedSubject);
        }
        return null;
    }

    @Override
    public SubjectResponse inactiveSubject(int id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
        if (subject.getStatus() == SubjectStatus.PUBLIC) {
            subject.setStatus(SubjectStatus.IN_ACTIVE);
            Subject updatedSubject = subjectRepository.save(subject);
            return subjectMapper.toSubjectResponse(updatedSubject);
        }
        return null;
    }

    @Override
    public SubjectResponse createSubject(SubjectRequest subjectRequest) {
        if (subjectRepository.existsBySubjectCode(subjectRequest.getSubjectCode())) {
            throw new AppException(ErrorEnum.SUBJECT_CODE_EXISTS);
        }
        if (subjectRepository.existsBySubjectName(subjectRequest.getSubjectName())) {
            throw new AppException(ErrorEnum.SUBJECT_NAME_EXISTS);
        }
        Subject subject = subjectMapper.toSubject(subjectRequest);
        subject.setStatus(SubjectStatus.DRAFT);
        subject.setIsDeleted(false);
        return subjectMapper.toSubjectResponse(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponse updateSubject(int id, SubjectRequest subjectRequest) throws AppException {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
        if (subjectRequest.getSubjectCode() != null) {
            if (subjectRepository.existsBySubjectCodeAndSubjectIdNot(subjectRequest.getSubjectCode(), id)) {
                throw new AppException(ErrorEnum.SUBJECT_CODE_EXISTS);
            }
            subject.setSubjectCode(subjectRequest.getSubjectCode());
        }
        if (subjectRequest.getSubjectName() != null) {
            if (subjectRepository.existsBySubjectNameAndSubjectIdNot(subjectRequest.getSubjectName(), id)) {
                throw new AppException(ErrorEnum.SUBJECT_NAME_EXISTS);
            }
            subject.setSubjectName(subjectRequest.getSubjectName());
        }
        if (subjectRequest.getDescription() != null) {
            subject.setDescription(subjectRequest.getDescription());
        }
        if (subjectRequest.getStatus() != null) {
            subject.setStatus(subjectRequest.getStatus());
        }
        if (subjectRequest.getThumbnailUrl() != null) {
            subject.setThumbnailUrl(subjectRequest.getThumbnailUrl());
        }
        subject.setStatus(SubjectStatus.DRAFT);
        Subject updatedSubject = subjectRepository.save(subject);
        return subjectMapper.toSubjectResponse(updatedSubject);
    }

    @Override
    public void deleteSubject(int id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));

        subject.setStatus(SubjectStatus.DRAFT);
        subject.setIsDeleted(true);
        subjectRepository.save(subject);
    }

    @Override
    public SubjectResponse getSubjectById(int id) {
        return subjectRepository.findById(id)
                .map(s -> {
                    SubjectResponse response = subjectMapper.toSubjectResponse(s);
                    response.setCountLessons(Math.max(lessonRepository.countAllBySubject_SubjectIdAndStatus(s.getSubjectId(), LessonStatus.PUBLIC), 0));
                    response.setCountUsers(Math.max(progressSubjectRepository.countUserBySubject_SubjectId(s.getSubjectId()), 0));
                    return response;
                })
                .orElseThrow(() -> new AppException(ErrorEnum.SUBJECT_NOT_FOUND));
    }

}
