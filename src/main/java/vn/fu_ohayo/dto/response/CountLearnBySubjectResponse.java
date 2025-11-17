package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CountLearnBySubjectResponse {
    String subject;
    int subjectId;
   int countLearn;
    int countAll;
    double average;
}
