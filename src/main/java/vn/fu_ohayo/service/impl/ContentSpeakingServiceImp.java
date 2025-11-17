package vn.fu_ohayo.service.impl;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.dto.response.PronunciationResultResponse;
import vn.fu_ohayo.entity.*;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.JlptLevel;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ContentMapper;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.service.ContentSpeakingService;
import vn.fu_ohayo.service.DialogueService;
import vn.fu_ohayo.service.ProgressDialogueService;
import vn.fu_ohayo.service.UserService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class ContentSpeakingServiceImp implements ContentSpeakingService {
    private final ContentSpeakingRepository contentSpeakingRepository;
    private final ContentMapper contentMapper;
    private final DialogueService dialogueService;
    private final ProgressDialogueService progressDialogueService;
    private final UserService userService;

    public ContentSpeakingServiceImp(ContentSpeakingRepository contentSpeakingRepository, ContentMapper contentMapper, DialogueService dialogueService, ProgressDialogueService progressDialogueService, UserService userService) {
        this.contentSpeakingRepository = contentSpeakingRepository;
        this.contentMapper = contentMapper;
        this.dialogueService = dialogueService;
        this.progressDialogueService = progressDialogueService;
        this.userService = userService;
    }

    @Value("${azure.speech.key}")
    private String azureSpeechKey;

    @Value("${azure.speech.region}")
    private String azureSpeechRegion;

    @Override
    public List<ContentSpeaking> getAllContentSpeakings() {
        return contentSpeakingRepository.findAll();
    }

    @Override
    public ContentSpeaking getContentSpeakingById(long id) throws AppException {
        return contentSpeakingRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.INVALID_CONTENT_SPEAKING));
    }

    @Override
    public ContentSpeaking handleCreateContentSpeaking(ContentSpeakingRequest contentSpeakingRequest) {
        Content newContent = Content.builder()
                .contentType(ContentTypeEnum.Speaking)
                .build();
        ContentSpeaking contentSpeaking = ContentSpeaking.builder()
                .image(contentSpeakingRequest.getImage())
                .title(contentSpeakingRequest.getTitle())
                .category(contentSpeakingRequest.getCategory())
                .status(ContentStatus.DRAFT)
                .jlptLevel(contentSpeakingRequest.getJlptLevel())
                .content(newContent)
                .build();
        return contentSpeakingRepository.save(contentSpeaking);
    }

    @Override
    public void deleteContentSpeakingById(long id) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        contentSpeaking.setDeleted(true);
        contentSpeakingRepository.save(contentSpeaking);
    }

    @Override
    public void deleteContentSpeakingByIdLastly(long id) {
        dialogueService.deleteDialogueByContenSpeaking(getContentSpeakingById(id));
        contentSpeakingRepository.deleteById(id);
    }

    @Override
    public ContentSpeaking getContentSpeakingByContent(Content content) {
        return contentSpeakingRepository.findByContent(content);
    }

    @Override
    public ContentSpeakingResponse updatePutContentSpeaking(long id, ContentSpeakingRequest request) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        if (contentSpeaking != null) {
            contentSpeakingRepository.save(contentMapper.contentSpeakingRequestToContentSpeaking(contentSpeaking, request));
        }
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }

    @Override
    public ContentSpeakingResponse updatePatchContentSpeaking(long id, ContentSpeakingRequest request) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        if (contentSpeaking != null) {
            boolean isUpdated = false;
            if (request.getImage() != null && !request.getImage().equals(contentSpeaking.getImage())) {
                contentSpeaking.setImage(request.getImage());
                isUpdated = true;
            }
            if (request.getTitle() != null && !request.getTitle().equals(contentSpeaking.getTitle())) {
                contentSpeaking.setTitle(request.getTitle());
                isUpdated = true;
            }
            if (request.getCategory() != null && !request.getCategory().equals(contentSpeaking.getCategory())) {
                contentSpeaking.setCategory(request.getCategory());
                isUpdated = true;
            }
            if (request.getJlptLevel() != null && !request.getJlptLevel().equals(contentSpeaking.getJlptLevel())) {
                contentSpeaking.setJlptLevel(request.getJlptLevel());
                isUpdated = true;
            }
            if (isUpdated) {
                contentSpeaking.setStatus(ContentStatus.DRAFT);
            }
            contentSpeakingRepository.save(contentSpeaking);
        }
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }

    @Override
    public Page<ContentSpeakingResponse> getContentSpeakingPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentSpeaking> prs = contentSpeakingRepository.findAllByDeleted(pageable, false);
        Page<ContentSpeakingResponse> responsePage = prs.map(contentMapper::toContentSpeakingResponse);
        return responsePage;
    }

    @Override
    public ContentSpeakingResponse acceptContentSpeaking(long id) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        boolean hasPublicDialogue = contentSpeaking.getDialogues() !=null  && !contentSpeaking.getDialogues().isEmpty();

        if (!hasPublicDialogue) {
            throw new AppException(ErrorEnum.CAN_NOT_ACCEPT);
        }
        contentSpeaking.setStatus(ContentStatus.PUBLIC);
        contentSpeakingRepository.save(contentSpeaking);
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }

    @Override
    public ContentSpeakingResponse rejectContentSpeaking(long id) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        contentSpeaking.setStatus(ContentStatus.REJECT);
        contentSpeakingRepository.save(contentSpeaking);
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }

    @Override
    public ContentSpeakingResponse inActiveContentSpeaking(long id) {
        ContentSpeaking contentSpeaking = getContentSpeakingById(id);
        contentSpeaking.setStatus(ContentStatus.IN_ACTIVE);
        contentSpeakingRepository.save(contentSpeaking);
        return contentMapper.toContentSpeakingResponse(contentSpeaking);
    }

    @Override
    public List<ContentSpeakingResponse> getListContentSpeakingBylever(JlptLevel jlptLevel) {
        return contentSpeakingRepository.findAllByJlptLevel(jlptLevel).stream().map(contentMapper::toContentSpeakingResponse).collect(Collectors.toList());
    }

    @Override
    public PronunciationResultResponse assessPronunciation(MultipartFile audioFile, long dialogueId) throws Exception {
        Dialogue dialogue = dialogueService.getDialogueById(dialogueId);
        if (dialogue == null) {
            throw new AppException(ErrorEnum.DIALOGUE_NOT_FOUND);
        }
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("File âm thanh không được để trống");
        }

        if (azureSpeechKey == null || azureSpeechKey.isEmpty() || azureSpeechRegion == null || azureSpeechRegion.isEmpty()) {
            throw new IllegalStateException("Cấu hình Azure Speech không hợp lệ");
        }

        File wavFile = null;
        SpeechConfig speechConfig = null;
        AudioConfig audioConfig = null;
        SpeechRecognizer recognizer = null;
        PronunciationAssessmentConfig pronunConfig = null;
        SpeechRecognitionResult result = null;

        try {
            wavFile = convertWebmToWav(audioFile);
            if (wavFile.length() == 0) {
                throw new IOException("File WAV sau chuyển đổi bị trống");
            }

            speechConfig = SpeechConfig.fromSubscription(azureSpeechKey, azureSpeechRegion);
            speechConfig.setSpeechRecognitionLanguage("ja-JP");

            audioConfig = AudioConfig.fromWavFileInput(wavFile.getAbsolutePath());
            pronunConfig = new PronunciationAssessmentConfig(
                    dialogue.getAnswerJp(),
                    PronunciationAssessmentGradingSystem.HundredMark,
                    PronunciationAssessmentGranularity.Phoneme,
                    true
            );
            pronunConfig.enableProsodyAssessment();

            recognizer = new SpeechRecognizer(speechConfig, audioConfig);
            pronunConfig.applyTo(recognizer);

            Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
            result = task.get(30, TimeUnit.SECONDS);

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                PronunciationAssessmentResult paResult = PronunciationAssessmentResult.fromResult(result);
                PronunciationResult pronunciationResult = PronunciationResult.builder()
                        .pronunciationScore(paResult.getPronunciationScore())
                        .accuracyScore(paResult.getAccuracyScore())
                        .fluencyScore(paResult.getFluencyScore())
                        .completenessScore(paResult.getCompletenessScore())
                        .prosodyScore(paResult.getProsodyScore())
                        .recognizedText(result.getText())
                        .build();
                handleCreateOrUpdateDialogueProgress(dialogue, pronunciationResult);
                return progressDialogueService.toPronunciationResultResponse(pronunciationResult);

            } else if (result.getReason() == ResultReason.NoMatch) {
                throw new RuntimeException("Không nhận diện được giọng nói phù hợp");
            } else if (result.getReason() == ResultReason.Canceled) {
                CancellationDetails cancellation = CancellationDetails.fromResult(result);
                throw new RuntimeException("Lỗi từ Azure Speech: " + cancellation.getErrorDetails());
            } else {
                throw new RuntimeException("Lỗi không xác định: " + result.getReason());
            }

        } catch (TimeoutException e) {
            throw new RuntimeException("⏱️ Quá thời gian chờ khi chấm điểm phát âm", e);

        } finally {
            if (result != null) result.close();
            if (recognizer != null) recognizer.close();
            if (pronunConfig != null) pronunConfig.close();
            if (speechConfig != null) speechConfig.close();
            if (audioConfig != null) audioConfig.close();
            if (wavFile != null && wavFile.exists()) wavFile.delete();
        }
    }


    private File convertWebmToWav(MultipartFile webmFile) throws IOException, InterruptedException {
        if (!webmFile.getContentType().equals("audio/webm")) {
            throw new IllegalArgumentException("File phải có định dạng WebM");
        }

        File tempWebm = File.createTempFile("input", ".webm");
        File tempWav = File.createTempFile("output", ".wav");

        try {
            webmFile.transferTo(tempWebm);

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",  // Sử dụng từ PATH
                    "-y",      // Ghi đè file nếu tồn tại
                    "-i", tempWebm.getAbsolutePath(),
                    "-vn",    // Bỏ video nếu có
                    "-ar", "16000",
                    "-ac", "1",
                    "-acodec", "pcm_s16le",
                    tempWav.getAbsolutePath()
            );

            // Chuyển hướng lỗi để tránh deadlock
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Đọc luồng đầu ra trong luồng riêng (quan trọng)
            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[FFmpeg] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            outputThread.start();

            int exitCode = process.waitFor();
            outputThread.join();  // Đảm bảo đọc xong log

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg lỗi (Mã: " + exitCode + ")");
            }

            if (tempWav.length() == 0) {
                throw new IOException("File WAV trống sau chuyển đổi");
            }

            return tempWav;
        } finally {
            if (tempWebm.exists()) tempWebm.delete();
        }
    }

    private void handleCreateOrUpdateDialogueProgress(Dialogue dialogue, PronunciationResult pronunciationResult) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new AppException(ErrorEnum.USER_NOT_FOUND);
        }
        progressDialogueService.handleCreateOrUpdateDialogueProgress(user, dialogue, pronunciationResult);
    }

    @Override
    public Page<ContentSpeakingResponse> getContentSpeakingPublicPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ContentSpeaking> prs = contentSpeakingRepository.findAllByDeletedAndStatus(pageable, false, ContentStatus.PUBLIC);
        Page<ContentSpeakingResponse> responsePage = prs.map(contentMapper::toContentSpeakingResponse);
        return responsePage;
    }

}
