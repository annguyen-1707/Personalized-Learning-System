package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ContentStatus;

import java.util.List;

public interface ContentReadingService {
    //    List<ContentReading> getAllContentReading() ;
//    ContentReading getContentReadingByContent(Content content);
//    ContentReadingResponse updatePutContentSpeaking(long id, ContentReadingRequest contentReadingRequest);
    ContentReading getContentReadingById(Long id);
    ContentReading handleCreateContentReading(ContentReadingRequest contentReadingRequest);
    void deleteContentReadingById(Long id);
    void deleteContentReadingByIdLastly(long id);
    ContentReadingResponse updatePatchContentReading(long id, ContentReadingRequest request);
    Page<ContentReadingResponse> getContentReadingPage(int page, int size);
    ContentReadingVocabularyResponse addVocabularyToContentReading(Long contentReadingId, int vocabularyId );
    void removeVocabularyFromContentReading(Long contentReadingId, int vocabularyId);
    ContentReadingGrammarResponse addGrammarToContentReading(Long contentReadingId, int grammarId);
    void removeGrammarFromContentReading(Long contentReadingId, int grammarId);
    List<VocabularyResponse> getVocabulariesByContentReadingId(long contentReadingId);
    List<GrammarResponse> getGrammarsByContentReadingId(long contentReadingId);
    ContentReadingResponse acceptContentReading(long id);
    ContentReadingResponse rejectContentReading(long id);
    ContentReadingResponse inActiveContentReading(long id);
    Page<ContentReadingResponse> getContentReadingPublicPage(int page, int size);
}
