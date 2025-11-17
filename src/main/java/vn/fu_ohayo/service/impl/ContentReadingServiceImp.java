package vn.fu_ohayo.service.impl;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.mapper.GrammarMapper;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.ContentReadingRepository;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.VocabularyRepository;
import vn.fu_ohayo.service.ContentReadingService;
import java.util.List;

@Service
public class ContentReadingServiceImp implements ContentReadingService {

    private final ContentReadingRepository contentReadingRepository;
    private final ContentMapper contentMapper;
    private final VocabularyRepository vocabularyRepository;
    private final GrammarRepository grammarRepository;
    private final VocabularyMapper vocabularyMapper;
    private final GrammarMapper grammarMapper;

    public ContentReadingServiceImp(ContentReadingRepository contentReadingRepository, ContentMapper contentMapper, VocabularyRepository vocabularyRepository, GrammarRepository grammarRepository, VocabularyMapper vocabularyMapper, GrammarMapper grammarMapper) {
        this.contentReadingRepository = contentReadingRepository;
        this.contentMapper = contentMapper;
        this.vocabularyRepository = vocabularyRepository;
        this.grammarRepository = grammarRepository;
        this.vocabularyMapper = vocabularyMapper;
        this.grammarMapper = grammarMapper;
    }


    @Override
    public ContentReading getContentReadingById(Long id) {
        return contentReadingRepository.findById(id).orElseThrow( () -> new AppException(ErrorEnum.CONTENT_READING_NOT_FOUND));
    }

    @Override
    public ContentReading handleCreateContentReading(ContentReadingRequest contentReadingRequest) {
//        if (Arrays.stream(CategoryReadingEnum.values())
//                .noneMatch(e -> e.name().equals(contentReadingRequest.getCategory()))) {
//            throw new AppException(ErrorEnum.INVALID_CATEGORY_CONTENT_READING);
//        }
        Content newContent = Content.builder()
                .contentType(ContentTypeEnum.Reading)
                .build();
        ContentReading contentReading = ContentReading.builder()
                .content(newContent)
                .image(contentReadingRequest.getImage())
                .title(contentReadingRequest.getTitle())
                .category(contentReadingRequest.getCategory())
                .audioFile(contentReadingRequest.getAudioFile())
                .timeNew(contentReadingRequest.getTimeNew())
                .scriptJp(contentReadingRequest.getScriptJp())
                .scriptVn(contentReadingRequest.getScriptVn())
                .status(ContentStatus.DRAFT)
                .jlptLevel(contentReadingRequest.getJlptLevel())
                .build();
        return contentReadingRepository.save(contentReading);
    }

    @Override
    public void deleteContentReadingById(Long id) {
        ContentReading contentReading = getContentReadingById(id);
        contentReading.setDeleted(true);
        contentReadingRepository.save(contentReading);
    }

    @Override
    public void deleteContentReadingByIdLastly(long id) {
        contentReadingRepository.deleteById(id);
    }

    @Override
    public ContentReadingResponse updatePatchContentReading(long id, ContentReadingRequest request) {
        ContentReading contentReading = contentReadingRepository.findById(id).orElse(null);
        if (contentReading != null) {
            boolean isUpdated = false;
            if (request.getImage() != null && !request.getImage().equals(contentReading.getImage())) {
                contentReading.setImage(request.getImage());
                isUpdated = true;
            }
            if (request.getTitle() != null && !request.getTitle().equals(contentReading.getTitle())) {
                contentReading.setTitle(request.getTitle());
                isUpdated = true;
            }
            if (request.getCategory() != null && !request.getCategory().equals(contentReading.getCategory())) {
                contentReading.setCategory(request.getCategory());
                isUpdated = true;
            }
            if (request.getAudioFile() != null && !request.getAudioFile().equals(contentReading.getAudioFile())) {
                contentReading.setAudioFile(request.getAudioFile());
                isUpdated = true;
            }
            if (request.getScriptJp() != null && !request.getScriptJp().equals(contentReading.getScriptJp())) {
                contentReading.setScriptJp(request.getScriptJp());
                isUpdated = true;
            }
            if (request.getScriptVn() != null && !request.getScriptVn().equals(contentReading.getScriptVn())) {
                contentReading.setScriptVn(request.getScriptVn());
                isUpdated = true;
            }
            if (request.getTimeNew() != null && !request.getTimeNew().equals(contentReading.getTimeNew())) {
                contentReading.setTimeNew(request.getTimeNew());
                isUpdated = true;
            }
            if(request.getJlptLevel() != null && !request.getJlptLevel().equals(contentReading.getJlptLevel())) {
                contentReading.setJlptLevel(request.getJlptLevel());
                isUpdated = true;
            }
            if (isUpdated) {
                contentReading.setStatus(ContentStatus.DRAFT);
            }
            contentReadingRepository.save(contentReading);
        }
        return contentMapper.toContentReadingResponse(contentReading);
    }

    @Override
    public Page<ContentReadingResponse> getContentReadingPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentReading> prs = contentReadingRepository.findAllByDeleted(pageable, false);
        Page<ContentReadingResponse> responsePage = prs.map(contentMapper::toContentReadingResponse);
        return responsePage;    }

    @Override
    public Page<ContentReadingResponse> getContentReadingPublicPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentReading> prs = contentReadingRepository.findAllByStatusAndDeleted(ContentStatus.PUBLIC, false, pageable);
        return prs.map(contentMapper::toContentReadingResponse);
    }

    @Override
    public ContentReadingVocabularyResponse addVocabularyToContentReading(Long contentReadingId, int vocabularyId) {
        ContentReading contentReading = this.getContentReadingById(contentReadingId);
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND));
        if (contentReading.getVocabularies().contains(vocabulary)) {
            throw new AppException(ErrorEnum.VOCABULARY_ALREADY_EXISTS_IN_CONTENT_READING);
        }
        contentReading.getVocabularies().add(vocabulary);
        contentReadingRepository.save(contentReading);
        return contentMapper.toContentReadingVocabularyResponse(contentReading);
    }

    @Override
    public void removeVocabularyFromContentReading(Long contentReadingId, int vocabularyId) {
        ContentReading contentReading = contentReadingRepository.findById(contentReadingId)
                .orElseThrow(() -> new RuntimeException("ContentReading not found"));
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found"));

        // Tải đầy đủ vocabularies để tránh ConcurrentModificationException
        Hibernate.initialize(contentReading.getVocabularies());
        contentReading.getVocabularies().remove(vocabulary);
        contentReadingRepository.save(contentReading);
    }

    @Override
    public ContentReadingGrammarResponse addGrammarToContentReading(Long contentReadingId, int grammarId) {
        ContentReading contentReading = this.getContentReadingById(contentReadingId);
        Grammar grammar = grammarRepository.findById(grammarId)
                .orElseThrow(() -> new AppException(ErrorEnum.VOCABULARY_NOT_FOUND));
        if (contentReading.getVocabularies().contains(grammar)) {
            throw new AppException(ErrorEnum.GRAMMAR_ALREADY_EXISTS_IN_CONTENT_READING);
        }
        contentReading.getGrammars().add(grammar);
        contentReadingRepository.save(contentReading);
        return contentMapper.toContentReadingGrammarResponse(contentReading);
    }

    @Override
    public void removeGrammarFromContentReading(Long contentReadingId, int grammarId) {
        ContentReading contentReading = this.getContentReadingById(contentReadingId);
        Grammar grammar = grammarRepository.findById(grammarId)
                .orElseThrow(() -> new RuntimeException("Grammar not found"));

        // Tải đầy đủ grammar để tránh ConcurrentModificationException
        Hibernate.initialize(contentReading.getVocabularies());
        contentReading.getGrammars().remove(grammar);
        contentReadingRepository.save(contentReading);
    }

    @Override
    public List<VocabularyResponse> getVocabulariesByContentReadingId(long contentReadingId) {
        return this.getContentReadingById(contentReadingId).getVocabularies().stream()
                .map(vocabularyMapper::toVocabularyResponse)
                .toList();
    }

    @Override
    public List<GrammarResponse> getGrammarsByContentReadingId(long contentReadingId) {
        return this.getContentReadingById(contentReadingId).getGrammars().stream()
                .map(grammarMapper::toGrammarResponse)
                .toList();
    }

    @Override
    public ContentReadingResponse acceptContentReading(long id) {
        ContentReading contentReading = getContentReadingById(id);
        contentReading.setStatus(ContentStatus.PUBLIC);
        contentReadingRepository.save(contentReading);
        return contentMapper.toContentReadingResponse(contentReading);
    }

    @Override
    public ContentReadingResponse rejectContentReading(long id) {
        ContentReading contentReading = getContentReadingById(id);
        contentReading.setStatus(ContentStatus.REJECT);
        contentReadingRepository.save(contentReading);
        return contentMapper.toContentReadingResponse(contentReading);
    }

    @Override
    public ContentReadingResponse inActiveContentReading(long id) {
        ContentReading contentReading = getContentReadingById(id);
        contentReading.setStatus(ContentStatus.IN_ACTIVE);
        contentReadingRepository.save(contentReading);
        return contentMapper.toContentReadingResponse(contentReading);
    }
}
