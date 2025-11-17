package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.ContentListeningRequest;
import vn.fu_ohayo.dto.response.ContentListeningResponse;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.JlptLevel;

import java.util.List;

public interface ContentListeningService {
    ContentListening getContentListeningById(Long id);
    ContentListening handleCreateContentListening(ContentListeningRequest contentListeningRequest);
    void deleteContentListeningById(Long id);
    void deleteContentListeningLastlyById(Long id);
    ContentListeningResponse updatePatchContentListening(long id, ContentListeningRequest request);
    Page<ContentListeningResponse> getContentListeningPage(int page, int size);
    ContentListeningResponse acceptContentListening(long id);
    ContentListeningResponse rejectContentListening(long id);
    ContentListeningResponse inActiveContentListening(long id);
    List<ContentListeningResponse> getListContentListeningsBylever(JlptLevel jlptLevel);
    Page<ContentListeningResponse> getContentListeningPublicPage(int page, int size);
}
