package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name =  "pronunciation_result")
@Entity
@ToString(exclude = {"progressDialogue"})
public class PronunciationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pronunciation_result_id")
    private int pronunciationResultId;
    @Column(name = "recognized_text")
    private String recognizedText;
    @Column(name = "accuracy_score")
    private double accuracyScore;
    @Column(name = "fluency_score")
    private double fluencyScore;
    @Column(name = "completeness_score")
    private double completenessScore;
    @Column(name = "pronunciation_score")
    private double pronunciationScore;
    @Column(name = "prosody_score")
    private double prosodyScore;
    @OneToOne(mappedBy = "pronunciationResult")
    private ProgressDialogue progressDialogue;

}
