package vn.fu_ohayo.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class LearningProgressOverviewResponse {
    private int totalVocabularyLearn;
    private int totalVocabularyAllSubject;
    private int totalGrammarLearn;
    private int totalGrammarAllSubject;
    private int exerciseCompleted;
    private int exerciseAllSubject;

}
