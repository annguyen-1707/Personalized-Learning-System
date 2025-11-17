package vn.fu_ohayo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import vn.fu_ohayo.dto.request.VocabularyRequest;
import vn.fu_ohayo.dto.response.VocabularyResponse;
import vn.fu_ohayo.entity.Lesson;
import vn.fu_ohayo.entity.Vocabulary;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.enums.PartOfSpeech;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.VocabularyMapper;
import vn.fu_ohayo.repository.LessonRepository;
import vn.fu_ohayo.repository.VocabularyRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VocabularyServiceImpTest {

    @Mock
    private VocabularyRepository vocabularyRepository;
    @Mock
    private VocabularyMapper vocabularyMapper;
    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private VocabularyServiceImp vocabularyServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for handleSaveVocabulary(int lessonId, int vocabularyId)
    @Test
    void handleSaveVocabulary_success() {
        int lessonId = 1, vocabularyId = 2;
        Lesson lesson = new Lesson();
        lesson.setVocabularies(new ArrayList<>());
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setLessons(new HashSet<>());

        when(lessonRepository.getLessonByLessonId(lessonId)).thenReturn(Optional.of(lesson));
        when(vocabularyRepository.findById(vocabularyId)).thenReturn(Optional.of(vocabulary));
        when(lessonRepository.existsVocabularyInLesson(vocabularyId, lessonId)).thenReturn(false);

        vocabularyServiceImp.handleSaveVocabulary(lessonId, vocabularyId);

        assertTrue(lesson.getVocabularies().contains(vocabulary));
        assertTrue(vocabulary.getLessons().contains(lesson));
        verify(lessonRepository).save(lesson);
        verify(vocabularyRepository).save(vocabulary);
    }

    @Test
    void handleSaveVocabulary_lessonNotFound() {
        when(lessonRepository.getLessonByLessonId(anyInt())).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> vocabularyServiceImp.handleSaveVocabulary(100, 2));
        assertEquals(ErrorEnum.LESSON_NOT_FOUND.getMessage(), ex.getMessage() );
    }

    @Test
    void handleSaveVocabulary_vocabularyNotFound() {
        Lesson lesson = new Lesson();
        when(lessonRepository.getLessonByLessonId(anyInt())).thenReturn(Optional.of(lesson));
        when(vocabularyRepository.findById(anyInt())).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> vocabularyServiceImp.handleSaveVocabulary(1, 200));
        assertEquals(ErrorEnum.VOCABULARY_NOT_FOUND.getMessage(), ex.getMessage());
    }

    @Test
    void handleSaveVocabulary_vocabularyExists() {
        Lesson lesson = new Lesson();
        lesson.setVocabularies(new ArrayList<>());
        Vocabulary vocabulary = new Vocabulary();
        when(lessonRepository.getLessonByLessonId(anyInt())).thenReturn(Optional.of(lesson));
        when(vocabularyRepository.findById(anyInt())).thenReturn(Optional.of(vocabulary));
        when(lessonRepository.existsVocabularyInLesson(anyInt(), anyInt())).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> vocabularyServiceImp.handleSaveVocabulary(1, 1));
        assertEquals(ErrorEnum.VOCABULARY_EXISTS.getMessage(), ex.getMessage());
    }

    // Test: existing vocabulary found, not deleted (should throw)
    @Test
    void handleSaveVocabulary_existingNotDeleted_throws() {
        VocabularyRequest request = new VocabularyRequest();
        request.setKanji("k");
        request.setKana("ka");
        request.setMeaning("m");
        Vocabulary existing = new Vocabulary();
        existing.setDeleted(false);

        when(vocabularyRepository.findAllByKanjiAndKanaAndMeaning("k", "ka", "m")).thenReturn(existing);

        AppException ex = assertThrows(AppException.class, () -> vocabularyServiceImp.handleSaveVocabulary(request));
        assertEquals(ErrorEnum.VOCABULARY_EXISTS.getMessage(), ex.getMessage());
    }

    // Test: existing vocabulary found, deleted (should restore and update)
    @Test
    void handleSaveVocabulary_existingDeleted_restores() {
        VocabularyRequest request = new VocabularyRequest();
        request.setKanji("k");
        request.setKana("ka");
        request.setMeaning("m");
        Vocabulary existing = new Vocabulary();
        existing.setDeleted(true);
        existing.setVocabularyId(1);

        when(vocabularyRepository.findAllByKanjiAndKanaAndMeaning("k", "ka", "m")).thenReturn(existing);
        when(vocabularyRepository.findById(1)).thenReturn(Optional.of(existing));
        when(vocabularyRepository.existsDuplicateVocabularyExceptId(any(), any(), anyInt())).thenReturn(false); // for updatePutVocabulary
        when(vocabularyRepository.save(any())).thenReturn(existing);
        when(vocabularyMapper.toVocabularyResponse(any())).thenReturn(new VocabularyResponse());

        VocabularyResponse response = vocabularyServiceImp.handleSaveVocabulary(request);
        assertNotNull(response);
        assertFalse(existing.getDeleted());
    }

    // Test: no existing vocabulary (should create new)
    @Test
    void handleSaveVocabulary_newVocabulary_creates() {
        VocabularyRequest request = new VocabularyRequest();
        request.setKanji("k");
        request.setKana("ka");
        request.setMeaning("m");

        when(vocabularyRepository.findAllByKanjiAndKanaAndMeaning("k", "ka", "m")).thenReturn(null);
        Vocabulary newVocab = new Vocabulary();
        when(vocabularyMapper.toVocabulary(request)).thenReturn(newVocab);
        when(vocabularyRepository.save(any())).thenReturn(newVocab);
        when(vocabularyMapper.toVocabularyResponse(any())).thenReturn(new VocabularyResponse());

        VocabularyResponse response = vocabularyServiceImp.handleSaveVocabulary(request);
        assertNotNull(response);
        assertFalse(newVocab.getDeleted());
    }

    // Tests for updatePutVocabulary(int vocabularyId, VocabularyRequest vocabularyRequest)
    @Test
    void updatePutVocabulary_success_updateAllFields() {
        int vocabularyId = 1;
        Vocabulary vocabulary = new Vocabulary();
        VocabularyRequest request = new VocabularyRequest();
        request.setKanji("kanji");
        request.setKana("kana");
        request.setRomaji("romaji");
        request.setMeaning("meaning");
        request.setDescription("desc");
        request.setExample("ex");
        request.setPartOfSpeech(PartOfSpeech.NOUN);
        request.setJlptLevel(JlptLevel.N5);

        when(vocabularyRepository.findById(vocabularyId)).thenReturn(Optional.of(vocabulary));
        when(vocabularyRepository.existsDuplicateVocabularyExceptId(any(), any(), eq(vocabularyId))).thenReturn(false);
        Vocabulary saved = new Vocabulary();
        when(vocabularyRepository.save(any())).thenReturn(saved);
        VocabularyResponse response = new VocabularyResponse();
        when(vocabularyMapper.toVocabularyResponse(saved)).thenReturn(response);

        VocabularyResponse result = vocabularyServiceImp.updatePutVocabulary(vocabularyId, request);

        assertEquals(response, result);
        assertEquals("kanji", vocabulary.getKanji());
        assertEquals("kana", vocabulary.getKana());
        assertEquals("romaji", vocabulary.getRomaji());
        assertEquals("meaning", vocabulary.getMeaning());
        assertEquals("desc", vocabulary.getDescription());
        assertEquals("ex", vocabulary.getExample());
        assertEquals(PartOfSpeech.NOUN, vocabulary.getPartOfSpeech());
        assertEquals(JlptLevel.N5, vocabulary.getJlptLevel());
    }

    @Test
    void updatePutVocabulary_vocabularyNotFound() {
        when(vocabularyRepository.findById(anyInt())).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> vocabularyServiceImp.updatePutVocabulary(1, new VocabularyRequest()));
        assertEquals(ErrorEnum.VOCABULARY_NOT_FOUND.getMessage(), ex.getMessage());
    }

    @Test
    void updatePutVocabulary_duplicateVocabulary() {
        Vocabulary vocabulary = new Vocabulary();
        when(vocabularyRepository.findById(anyInt())).thenReturn(Optional.of(vocabulary));
        when(vocabularyRepository.existsDuplicateVocabularyExceptId(any(), any(), anyInt())).thenReturn(true);

        VocabularyRequest request = new VocabularyRequest();
        request.setKanji("k");
        request.setMeaning("m");

        AppException ex = assertThrows(AppException.class, () -> vocabularyServiceImp.updatePutVocabulary(1, request));
        assertEquals(ErrorEnum.VOCABULARY_EXISTS.getMessage(), ex.getMessage());
    }

    @Test
    void updatePutVocabulary_updatePartialFields() {
        Vocabulary vocabulary = new Vocabulary();
        when(vocabularyRepository.findById(anyInt())).thenReturn(Optional.of(vocabulary));
        when(vocabularyRepository.existsDuplicateVocabularyExceptId(any(), any(), anyInt())).thenReturn(false);
        when(vocabularyRepository.save(any())).thenReturn(vocabulary);
        VocabularyResponse response = new VocabularyResponse();
        when(vocabularyMapper.toVocabularyResponse(any())).thenReturn(response);

        VocabularyRequest request = new VocabularyRequest();
        request.setKanji("kanji");
        // other fields are null

        VocabularyResponse result = vocabularyServiceImp.updatePutVocabulary(1, request);

        assertEquals("kanji", vocabulary.getKanji());
        assertEquals(response, result);
    }

    @Test
    void updatePutVocabulary_noFieldsUpdated() {
        Vocabulary vocabulary = new Vocabulary();
        when(vocabularyRepository.findById(anyInt())).thenReturn(Optional.of(vocabulary));
        when(vocabularyRepository.existsDuplicateVocabularyExceptId(any(), any(), anyInt())).thenReturn(false);
        when(vocabularyRepository.save(any())).thenReturn(vocabulary);
        VocabularyResponse response = new VocabularyResponse();
        when(vocabularyMapper.toVocabularyResponse(any())).thenReturn(response);

        VocabularyRequest request = new VocabularyRequest(); // all fields null

        VocabularyResponse result = vocabularyServiceImp.updatePutVocabulary(1, request);

        assertEquals(response, result);
        // Optionally assert that vocabulary fields remain null
        assertNull(vocabulary.getKanji());
        assertNull(vocabulary.getKana());
        assertNull(vocabulary.getRomaji());
        assertNull(vocabulary.getMeaning());
        assertNull(vocabulary.getDescription());
        assertNull(vocabulary.getExample());
        assertNull(vocabulary.getPartOfSpeech());
        assertNull(vocabulary.getJlptLevel());

    }

}