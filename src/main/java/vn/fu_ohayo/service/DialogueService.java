package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.DialogueRequest;
import vn.fu_ohayo.dto.response.DialogueResponse;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.entity.Dialogue;

import java.util.List;

public interface DialogueService {
    List<Dialogue> getAllDialogues();
    Dialogue getDialogueById(long id);
    Dialogue handleSaveDialogue(DialogueRequest dialogueRequest);
    void softDeleteDialogueById(long id);
    void hardDeleteDialogueById(long id);
    Dialogue updatePatchDialogue(long id, Dialogue dialogue);
    List<DialogueResponse> getDialoguesByContentSpeakingId(long contentSpeakingId);
    Page<DialogueResponse> getDialoguePage(int page, int size,long contentSpeakingId);
    void deleteDialogueByContenSpeaking(ContentSpeaking contentSpeaking);
    Page<DialogueResponse> getAllDialoguePage(int page, int size);
    Page<DialogueResponse> getAllDialogueEmpty(int page, int size);
    DialogueResponse addDialogueIntoContentSpeaking(long dialogueId, long contentSpeakingId);
    void removeDialogueFromContentSpeaking(long dialogueId);
}
