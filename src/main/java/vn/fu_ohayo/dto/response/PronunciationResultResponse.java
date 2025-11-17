package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PronunciationResultResponse {
    private String recognizedText;
    private double accuracyScore;
    private double fluencyScore;
    private double completenessScore;
    private double pronunciationScore;
    private double prosodyScore;
    List<String> advices;
}
