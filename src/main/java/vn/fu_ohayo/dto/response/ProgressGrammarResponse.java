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
public class ProgressGrammarResponse {
    List<CountLearnBySubjectResponse> countLearnBySubjectResponses;
    List<RecentlyLearnGrammarResponse> recentlyLearnGrammarResponses;
}
