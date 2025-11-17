package vn.fu_ohayo.service.impl;

import jdk.jshell.Snippet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentListeningRequest;
import vn.fu_ohayo.dto.response.ContentListeningResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ExerciseQuestion;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.repository.ContentListeningRepository;
import vn.fu_ohayo.service.ContentListeningService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContentListeningServiceImp implements ContentListeningService {
    private final ContentListeningRepository contentListeningRepository;
    private final ContentMapper contentMapper;

    public ContentListeningServiceImp(ContentListeningRepository contentListeningRepository, ContentMapper contentMapper) {
        this.contentListeningRepository = contentListeningRepository;
        this.contentMapper = contentMapper;
    }


    @Override
    public ContentListening getContentListeningById(Long id) {
        ContentListening contentListening = contentListeningRepository.findBycontentListeningId(id);
        return contentListening;
    }

    @Override
    public ContentListening handleCreateContentListening(ContentListeningRequest contentListeningRequest) {
        Content newContent = Content.builder()
                .contentType(ContentTypeEnum.Listening)
                .build();
        ContentListening contentListening = ContentListening.builder()
                .content(newContent)
                .image(contentListeningRequest.getImage())
                .title(contentListeningRequest.getTitle())
                .category(contentListeningRequest.getCategory())
                .audioFile(contentListeningRequest.getAudioFile())
                .scriptJp(contentListeningRequest.getScriptJp())
                .scriptVn(contentListeningRequest.getScriptVn())
                .status(ContentStatus.DRAFT)
                .jlptLevel(contentListeningRequest.getJlptLevel())
                .build();
        return contentListeningRepository.save(contentListening);
    }

    @Override
    public void deleteContentListeningById(Long id) {
        ContentListening contentListening = getContentListeningById(id);
        contentListening.setDeleted(true);
        contentListeningRepository.save(contentListening);
    }

    @Override
    public void deleteContentListeningLastlyById(Long id) {
        contentListeningRepository.deleteById(id);
    }

    @Override
    public ContentListeningResponse updatePatchContentListening(long id, ContentListeningRequest request) {
        ContentListening contentListening = contentListeningRepository.findBycontentListeningId(id);
        if (contentListening != null) {
            boolean isUpdated = false;
            if (request.getImage() != null && !request.getImage().equals(contentListening.getImage())) {
                contentListening.setImage(request.getImage());
                isUpdated = true;
            }
            if (request.getTitle() != null && !request.getTitle().equals(contentListening.getTitle())) {
                contentListening.setTitle(request.getTitle());
                isUpdated = true;
            }
            if (request.getCategory() != null && !request.getCategory().equals(contentListening.getCategory())) {
                contentListening.setCategory(request.getCategory());
                isUpdated = true;
            }
            if (request.getAudioFile() != null && !request.getAudioFile().equals(contentListening.getAudioFile())) {
                contentListening.setAudioFile(request.getAudioFile());
                isUpdated = true;
            }
            if (request.getScriptJp() != null && !request.getScriptJp().equals(contentListening.getScriptJp())) {
                contentListening.setScriptJp(request.getScriptJp());
                isUpdated = true;
            }
            if (request.getScriptVn() != null && !request.getScriptVn().equals(contentListening.getScriptVn())) {
                contentListening.setScriptVn(request.getScriptVn());
                isUpdated = true;
            }
            if (request.getJlptLevel() != null && !request.getJlptLevel().equals(contentListening.getJlptLevel())) {
                contentListening.setJlptLevel(request.getJlptLevel());
                isUpdated = true;
            }
            if (isUpdated) {
                contentListening.setStatus(ContentStatus.DRAFT);
            }
            contentListeningRepository.save(contentListening);
        }
        return contentMapper.toContentListeningResponse(contentListening);
    }

    @Override
    public Page<ContentListeningResponse> getContentListeningPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentListening> prs = contentListeningRepository.findAllByDeleted(pageable, false);
        Page<ContentListeningResponse> responsePage = prs.map(contentMapper::toContentListeningResponse);
        return responsePage;
    }
    @Override
    public Page<ContentListeningResponse> getContentListeningPublicPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentListening> prs = contentListeningRepository.findAllByStatusAndDeleted(ContentStatus.PUBLIC, false, pageable);
        Page<ContentListeningResponse> responsePage = prs.map(contentMapper::toContentListeningResponse);
        return responsePage;
    }

    @Override
    public ContentListeningResponse acceptContentListening(long id) {
        ContentListening contentListening = getContentListeningById(id);
        Set<ExerciseQuestion> exerciseQuestions = contentListening.getExerciseQuestions();
        boolean hasQuestion = contentListening.getExerciseQuestions() != null && !exerciseQuestions.isEmpty();
        if (!hasQuestion) {
            throw new AppException(ErrorEnum.CAN_NOT_ACCEPT);
        }
            contentListening.setStatus(ContentStatus.PUBLIC);
        contentListeningRepository.save(contentListening);
        return contentMapper.toContentListeningResponse(contentListening);
    }

    @Override
    public ContentListeningResponse rejectContentListening(long id) {
        ContentListening contentListening = getContentListeningById(id);
        contentListening.setStatus(ContentStatus.REJECT);
        contentListeningRepository.save(contentListening);
        return contentMapper.toContentListeningResponse(contentListening);
    }

    @Override
    public ContentListeningResponse inActiveContentListening(long id) {
        ContentListening contentListening = getContentListeningById(id);
        contentListening.setStatus(ContentStatus.IN_ACTIVE);
        contentListeningRepository.save(contentListening);
        return contentMapper.toContentListeningResponse(contentListening);
    }

    @Override
    public List<ContentListeningResponse> getListContentListeningsBylever(JlptLevel jlptLevel) {
        return contentListeningRepository.findAllByJlptLevel(jlptLevel).stream().map(contentMapper::toContentListeningResponse).collect(Collectors.toList());
    }

}
