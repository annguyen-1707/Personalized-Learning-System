package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.Content;
import java.util.List;

public interface ContentService {
    List<Content> getAllContents();
    Content getContentById(long id);
    Content handleSaveContent(Content content);
    void deleteContentById(long id);
}
