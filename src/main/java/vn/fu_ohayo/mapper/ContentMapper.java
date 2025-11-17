package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.fu_ohayo.dto.request.ContentListeningRequest;
import vn.fu_ohayo.dto.request.ContentReadingRequest;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.entity.ContentListening;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ContentSpeaking;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    ContentSpeaking contentSpeakingRequestToContentSpeaking(@MappingTarget ContentSpeaking contentSpeaking, ContentSpeakingRequest contentSpeakingRequest);

    ContentSpeakingResponse toContentSpeakingResponse(ContentSpeaking contentSpeaking);

    ContentListening contentListeningRequestToContentListening(@MappingTarget ContentListening contentListening, ContentListeningRequest contentListeningRequest);

    ContentListeningResponse toContentListeningResponse(ContentListening contentListening);

    ContentReading contentReadingRequestToContentReading(@MappingTarget ContentReading contentReading, ContentReadingRequest contentReadingRequest);

    ContentReadingResponse toContentReadingResponse(ContentReading contentReading);

    ContentReadingVocabularyResponse toContentReadingVocabularyResponse(ContentReading contentReading);

    ContentReadingGrammarResponse toContentReadingGrammarResponse(ContentReading contentReading);
}
