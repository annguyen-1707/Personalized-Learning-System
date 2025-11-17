package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ProgressDialogueResponse;
import vn.fu_ohayo.dto.response.PronunciationResultResponse;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.entity.ProgressDialogue;
import vn.fu_ohayo.entity.PronunciationResult;
import vn.fu_ohayo.entity.User;

public interface ProgressDialogueService {
    ProgressDialogueResponse getProgressByUserAndDialogue(User user, Dialogue dialogue) ;
    ProgressDialogueResponse handleCreateOrUpdateDialogueProgress(User user, Dialogue dialogue, PronunciationResult pronunciationResult) ;
    PronunciationResultResponse toPronunciationResultResponse(PronunciationResult pronunciationResult);
}
