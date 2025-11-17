package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.DialogueRequest;
import vn.fu_ohayo.dto.response.DialogueResponse;
import vn.fu_ohayo.entity.Dialogue;

@Mapper(componentModel = "spring")
public interface DialogueMapper {
    Dialogue toDialogue(DialogueRequest request);
    DialogueResponse toDialogueResponse(Dialogue dialogue);
}
