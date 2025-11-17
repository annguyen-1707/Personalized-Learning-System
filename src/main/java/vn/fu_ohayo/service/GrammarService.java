package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.request.PatchGrammarRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;

import java.util.List;

public interface GrammarService {
    GrammarResponse saveGrammar(GrammarRequest grammarRequest);
    GrammarResponse updateGrammar(int id, GrammarRequest grammarRequest);
    GrammarResponse patchGrammar(int id, PatchGrammarRequest patchGrammarRequest);
    void deleteGrammarById(int id);
    Page<GrammarResponse> getAllGrammars(int lessonId, int page, int size);
    Page<GrammarResponse> getAllGrammarsPage(int page, int size);

    Page<GrammarResponse> getGrammarsNotInLesson(int lessonId, int page, int size);

    void deleteGrammarFromLesson(int id, int lessonId);

    void handleSaveGrammarIntoLesson(int lessonId, int grammarId);
}
