package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.response.DialogueResponse;
import vn.fu_ohayo.dto.response.ProgressDialogueResponse;
import vn.fu_ohayo.dto.response.PronunciationResultResponse;
import vn.fu_ohayo.entity.ProgressDialogue;

@Mapper(componentModel = "spring")
public interface ProgressDialogueMapper {
    ProgressDialogueResponse toProgressDialogueResponse(ProgressDialogue progressDialogue);

}
