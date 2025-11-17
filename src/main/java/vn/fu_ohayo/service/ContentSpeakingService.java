package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.dto.response.PronunciationResultResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.List;

public interface
ContentSpeakingService {
    List<ContentSpeaking> getAllContentSpeakings();

    ContentSpeaking getContentSpeakingById(long id);

    ContentSpeaking handleCreateContentSpeaking(ContentSpeakingRequest contentSpeakingRequest);

    void deleteContentSpeakingById(long id);

    void deleteContentSpeakingByIdLastly(long id);

    ContentSpeaking getContentSpeakingByContent(Content content);

    ContentSpeakingResponse updatePutContentSpeaking(long id, ContentSpeakingRequest contentSpeakingRequest);

    ContentSpeakingResponse updatePatchContentSpeaking(long id, ContentSpeakingRequest contentSpeakingRequest);

    Page<ContentSpeakingResponse> getContentSpeakingPage(int page, int size);

    ContentSpeakingResponse acceptContentSpeaking(long id);

    ContentSpeakingResponse rejectContentSpeaking(long id);

    ContentSpeakingResponse inActiveContentSpeaking(long id);

    List<ContentSpeakingResponse> getListContentSpeakingBylever(JlptLevel jlptLevel);

    PronunciationResultResponse assessPronunciation(MultipartFile audioFile, long dialogueId) throws Exception;

    Page<ContentSpeakingResponse> getContentSpeakingPublicPage(int page, int size);

}
