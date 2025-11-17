package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.request.PatchGrammarRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.entity.Grammar;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.GrammarMapper;
import vn.fu_ohayo.repository.GrammarRepository;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.service.GrammarService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrammarServiceImp implements GrammarService {

    private final LessonRepository lessonRepository;
    private final GrammarRepository grammarRepository;
    private final GrammarMapper grammarMapper;

    @Override
    public GrammarResponse saveGrammar(GrammarRequest grammarRequest) {
        String normalizedTitleJp = grammarRequest.getTitleJp().replace("〜", "~").replace("～", "~").trim();
        Grammar grammarOptional = grammarRepository.findByTitleJp(normalizedTitleJp);
        if (grammarOptional != null) {
            if(grammarOptional.getDeleted()) {
                grammarOptional.setDeleted(false);
                grammarOptional.setTitleJp(grammarRequest.getTitleJp());
                grammarOptional.setJlptLevel(grammarRequest.getJlptLevel());
                grammarOptional.setExample(grammarRequest.getExample());
                grammarOptional.setMeaning(grammarRequest.getMeaning());
                grammarOptional.setStructure(grammarRequest.getStructure());
                grammarOptional.setUsage(grammarRequest.getUsage());
                return grammarMapper.toGrammarResponse(grammarRepository.save(grammarOptional));
            } else {
                throw new AppException(ErrorEnum.GRAMMAR_EXISTED);
            }
        }

        Grammar grammar = Grammar.builder()
                .titleJp(grammarRequest.getTitleJp())
                .jlptLevel(grammarRequest.getJlptLevel())
                .example(grammarRequest.getExample())
                .meaning(grammarRequest.getMeaning())
                .structure(grammarRequest.getStructure())
                .usage(grammarRequest.getUsage())
                .deleted(false)
                .build();
        return grammarMapper.toGrammarResponse(grammarRepository.save(grammar));
    }

    @Override
    public GrammarResponse updateGrammar(int id, GrammarRequest grammarRequest) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.GRAMMAR_NOT_FOUND));
        if(grammarRepository.existsByTitleJpAndMeaningAndGrammarIdNot(
                grammarRequest.getTitleJp(),
                grammarRequest.getMeaning(),
                id
        )){
            throw new AppException(ErrorEnum.GRAMMAR_EXISTED);
        }
        grammar.setTitleJp(grammarRequest.getTitleJp());
        grammar.setStructure(grammarRequest.getStructure());
        grammar.setMeaning(grammarRequest.getMeaning());
        grammar.setUsage(grammarRequest.getUsage());
        grammar.setExample(grammarRequest.getExample());
        grammar.setJlptLevel(grammarRequest.getJlptLevel());
        return grammarMapper.toGrammarResponse(grammarRepository.save(grammar));
    }

    @Override
    public GrammarResponse patchGrammar(int id, PatchGrammarRequest patchGrammarRequest) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.GRAMMAR_NOT_FOUND));
        if(grammarRepository.existsByTitleJpAndMeaningAndGrammarIdNot(
                patchGrammarRequest.getTitleJp(),
                patchGrammarRequest.getMeaning(),
                id
        )){
            throw new AppException(ErrorEnum.GRAMMAR_EXISTED);
        }
        if (patchGrammarRequest.getTitleJp() != null) {
            grammar.setTitleJp(patchGrammarRequest.getTitleJp());
        }
        if (patchGrammarRequest.getStructure() != null) {
            grammar.setStructure(patchGrammarRequest.getStructure());
        }
        if (patchGrammarRequest.getMeaning() != null) {
            grammar.setMeaning(patchGrammarRequest.getMeaning());
        }
        if (patchGrammarRequest.getUsage() != null) {
            grammar.setUsage(patchGrammarRequest.getUsage());
        }
        if (patchGrammarRequest.getExample() != null) {
            grammar.setExample(patchGrammarRequest.getExample());
        }
        if (patchGrammarRequest.getJlptLevel() != null) {
            grammar.setJlptLevel(patchGrammarRequest.getJlptLevel());
        }
        return grammarMapper.toGrammarResponse(grammarRepository.save(grammar));
    }

    @Override
    public void deleteGrammarById(int id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.GRAMMAR_NOT_FOUND));
        grammar.setDeleted(true);
        grammarRepository.save(grammar);
    }


    @Override
    public Page<GrammarResponse> getAllGrammars(int lessonId, int page, int size) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorEnum.LESSON_NOT_FOUND));
        Page<Grammar> grammarPage = grammarRepository.findAllByLessonIdAndDeletedIsFalse(lesson.getLessonId(), PageRequest.of(page, size));
        return grammarPage.map(grammarMapper::toGrammarResponse);
    }

    @Override
    public Page<GrammarResponse> getAllGrammarsPage(int page, int size) {
        return grammarRepository.findAll(PageRequest.of(page, size))
                .map(grammarMapper::toGrammarResponse);    }

    @Override
    public Page<GrammarResponse> getGrammarsNotInLesson(int lessonId, int page, int size) {
        return grammarRepository.findAllNotInLesson(lessonId, PageRequest.of(page, size))
                .map(grammarMapper::toGrammarResponse);
    }

    @Override
    public void deleteGrammarFromLesson(int id, int lessonId) {
        grammarRepository.removeGrammarInLessonId(id, lessonId);
    }

    @Override
    public void handleSaveGrammarIntoLesson(int lessonId, int grammarId) {
        grammarRepository.saveGrammarIntoLesson(lessonId, grammarId);
    }
}
