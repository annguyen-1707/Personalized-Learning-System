package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ProgressContentResponse;
import vn.fu_ohayo.entity.ContentReading;
import vn.fu_ohayo.entity.ProgressContent;
import vn.fu_ohayo.entity.User;

import java.util.List;

public interface ContentReadingProgressService {
    ApiResponse<String> markReadingProgress(Long userId, Long contentReadingId);
    Boolean isDoneReading(Long userId, Long contentReadingId);
    List<ProgressContentResponse> getCompletedReadings(String userId);
}
