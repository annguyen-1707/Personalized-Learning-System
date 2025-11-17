package vn.fu_ohayo.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    String content;
    int rating;
}
