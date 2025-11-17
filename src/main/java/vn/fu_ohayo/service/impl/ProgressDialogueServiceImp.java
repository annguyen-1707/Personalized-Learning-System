package vn.fu_ohayo.service.impl;

import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ProgressDialogueResponse;
import vn.fu_ohayo.dto.response.PronunciationResultResponse;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.entity.ProgressDialogue;
import vn.fu_ohayo.entity.PronunciationResult;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressDialogueMapper;
import vn.fu_ohayo.repository.ProgressDialogueRepository;
import vn.fu_ohayo.repository.PronunciationResultRepository;
import vn.fu_ohayo.service.ProgressDialogueService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressDialogueServiceImp implements ProgressDialogueService {
    private final ProgressDialogueMapper progressDialogueMapper;
    private final ProgressDialogueRepository progressDialogueRepository;
    private final PronunciationResultRepository pronunciationResultRepository;

    public ProgressDialogueServiceImp(ProgressDialogueMapper progressDialogueMapper, ProgressDialogueRepository progressDialogueRepository, PronunciationResultRepository pronunciationResultRepository) {
        this.progressDialogueMapper = progressDialogueMapper;
        this.progressDialogueRepository = progressDialogueRepository;
        this.pronunciationResultRepository = pronunciationResultRepository;
    }

    @Override
    public ProgressDialogueResponse getProgressByUserAndDialogue(User user, Dialogue dialogue) {
        ProgressDialogue progressDialogue = progressDialogueRepository.findByUserAndDialogue(user, dialogue);
        if (progressDialogue == null) {
            throw new AppException(ErrorEnum.PROGRESS_NOT_FOUND);
        }
        return progressDialogueMapper.toProgressDialogueResponse(progressDialogue);
    }

    @Override
    public ProgressDialogueResponse handleCreateOrUpdateDialogueProgress(User user, Dialogue dialogue, PronunciationResult pronunciationResult) {
        ProgressDialogue progressDialogue = progressDialogueRepository.findByUserAndDialogue(user, dialogue);

        if (progressDialogue == null) {
            progressDialogue = ProgressDialogue.builder()
                    .user(user)
                    .dialogue(dialogue)
                    .progressStatus(ProgressStatus.COMPLETED)
                    .build();
        } else {
            PronunciationResult existingResult = progressDialogue.getPronunciationResult();
            if (existingResult != null) {
                progressDialogue.setPronunciationResult(null); // 👉 Gỡ liên kết trước
                progressDialogueRepository.save(progressDialogue); // 👉 Save lại để tránh lỗi reference

                pronunciationResultRepository.delete(existingResult); // 👉 Bây giờ mới xoá
            }
        }

        pronunciationResult = pronunciationResultRepository.save(pronunciationResult); // 👉 Ensure saved instance
        progressDialogue.setPronunciationResult(pronunciationResult); // 👉 Gán sau khi đã lưu
        progressDialogue = progressDialogueRepository.save(progressDialogue); // 👉 Save cha sau cùng
        return progressDialogueMapper.toProgressDialogueResponse(progressDialogue);
    }

    @Override
    public PronunciationResultResponse toPronunciationResultResponse(PronunciationResult pronunciationResult) {
        return PronunciationResultResponse.builder()
                .prosodyScore(pronunciationResult.getProsodyScore())
                .fluencyScore(pronunciationResult.getFluencyScore())
                .pronunciationScore(pronunciationResult.getPronunciationScore())
                .completenessScore(pronunciationResult.getCompletenessScore())
                .accuracyScore(pronunciationResult.getAccuracyScore())
                .recognizedText(pronunciationResult.getRecognizedText())
                .advices(getAdviceFromAssessment(pronunciationResult))
                .build();
    }

    private List<String> getAdviceFromAssessment(PronunciationResult paResult) {
        List<String> adviceList = new ArrayList<>();

        // Accuracy: Độ chính xác so với câu mẫu
        if (paResult.getAccuracyScore() < 60) {
            adviceList.add("🔍 Phát âm nhiều từ chưa chính xác. Hãy luyện tập phát âm từng từ riêng biệt.");
        } else if (paResult.getAccuracyScore() < 85) {
            adviceList.add("✅ Phát âm tương đối chính xác, hãy chú ý hơn ở các âm tiết khó.");
        } else {
            adviceList.add("🌟 Phát âm rất chính xác, hãy tiếp tục giữ phong độ!");
        }

        // Completeness: Đọc đầy đủ từ không
        if (paResult.getCompletenessScore() < 60) {
            adviceList.add("🧩 Một số từ bị bỏ sót khi đọc. Hãy luyện tập đọc trọn vẹn câu.");
        } else if (paResult.getCompletenessScore() < 85) {
            adviceList.add("📖 Đa số từ đã được đọc, nhưng có thể luyện đọc rõ ràng hơn nữa.");
        } else {
            adviceList.add("✅ Bạn đọc đủ và rõ toàn bộ nội dung.");
        }

        // Fluency: Đọc trôi chảy
        if (paResult.getFluencyScore() < 60) {
            adviceList.add("⏳ Bạn đọc còn ngắt quãng. Hãy luyện đọc với nhịp điệu đều.");
        } else if (paResult.getFluencyScore() < 85) {
            adviceList.add("🗣️ Bạn đọc khá trôi chảy, nhưng nên cải thiện thêm ngữ điệu.");
        } else {
            adviceList.add("🎯 Bạn đọc rất trôi chảy và tự nhiên.");
        }

        // Prosody: Ngữ điệu, nhấn âm
        if (paResult.getProsodyScore() < 60) {
            adviceList.add("📉 Ngữ điệu chưa tự nhiên. Hãy học cách lên xuống giọng như người bản xứ.");
        } else if (paResult.getProsodyScore() < 85) {
            adviceList.add("🎵 Ngữ điệu tương đối ổn, nên luyện thêm với các bài đọc có nhấn âm.");
        } else {
            adviceList.add("🌈 Ngữ điệu rất tự nhiên, tuyệt vời!");

        }
        // Pronunciation: Tổng thể phát âm
        if (paResult.getPronunciationScore() < 60) {
            adviceList.add("📌Tổng kết: Phát âm còn yếu. Nên luyện từng phần kỹ hơn để cải thiện.");
        } else if (paResult.getPronunciationScore() < 85) {
            adviceList.add("📌Tổng kết: Phát âm khá ổn, có tiềm năng cải thiện lên mức cao hơn.");
        } else {
            adviceList.add("📌Tổng kết: Phát âm xuất sắc, bạn đang ở trình độ gần như bản ngữ.");
        }

        return adviceList;
    }

}
