package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.repository.ContentRepository;
import vn.fu_ohayo.service.ContentService;

import java.util.List;

@Service
public class ContentServiceImp implements ContentService {

    ContentRepository contentRepository;
    public ContentServiceImp(ContentRepository contentRepository) {}

    @Override
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    @Override
    public Content getContentById(long id) {
        return contentRepository.findById(id).orElse(null);
    }

    public Content handleSaveContent(Content content) {
        return contentRepository.save(content);
    }

    public void deleteContentById(long id) {
        contentRepository.deleteById(id);
    }


}
