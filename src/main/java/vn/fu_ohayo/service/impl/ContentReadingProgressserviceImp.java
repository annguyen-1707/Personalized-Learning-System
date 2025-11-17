package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressContentResponse;
import vn.fu_ohayo.entity.Content;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.ProgressStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.ProgressContentMapper;
import vn.fu_ohayo.repository.ContentReadingRepository;
import vn.fu_ohayo.repository.ProgressContentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.ContentReadingProgressService;
import vn.fu_ohayo.service.ProgressContentService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentReadingProgressserviceImp implements ContentReadingProgressService {
    private final UserRepository userRepository;
    private final ContentReadingRepository contentReadingRepository;
    private final ProgressContentRepository progressContentRepository;
    private final ProgressContentMapper progressContentMapper;

    @Override
    public ApiResponse<String> markReadingProgress(Long userId, Long contentReadingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found" + userId));
        ContentReading contentReading = contentReadingRepository.findById(contentReadingId)
                .orElseThrow(() -> new RuntimeException("Content reading not found" + contentReadingId));
        ProgressContent progressContent = progressContentRepository.findByUserAndContent(user, contentReading.getContent())
                .orElseGet(() -> ProgressContent.builder()
                        .user(user)
                        .content(contentReading.getContent())
                        .build());
        progressContent.setProgressStatus(ProgressStatus.COMPLETED);
        //set thoi gian hoan thanh
//        progressContent.setCreatedAt(Date.from(LocalDateTime.now().toInstant()));
        progressContentRepository.save(progressContent);
        return ApiResponse.<String>builder()
                .status("200")
                .message("Reading progress marked successfully")
                .data("Content reading progress marked for user: " + userId + " and content reading: " + contentReadingId)
                .build();
    }

    @Override
    public Boolean isDoneReading(Long userId, Long contentReadingId) {
        ContentReading contentReading = contentReadingRepository.findById(contentReadingId)
                .orElseThrow(() -> new RuntimeException("Content reading not found" + contentReadingId));

        Content content = contentReading.getContent();
        if (content == null) {
            throw new RuntimeException("Content not found for content reading ID: " + contentReadingId);
        }

        return progressContentRepository.findByUser_UserIdAndContent_ContentId(userId, content.getContentId())
                .map(pc -> pc.getProgressStatus() == ProgressStatus.COMPLETED)
                .orElse(false);
    }
    @Override
    public List<ProgressContentResponse> getCompletedReadings(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorEnum.USER_NOT_FOUND)
        );
        return progressContentRepository.findAllByUserAndProgressStatus(user, ProgressStatus.COMPLETED).stream().map(
                progressContentMapper::toResponse
        ).toList();
    }
}
