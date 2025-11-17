package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    String question;

    @OneToOne
    @JoinColumn(name =  "vocabulary_id", unique = true)
    private Vocabulary vocabulary;

    @OneToOne
    @JoinColumn(name =  "grammar_id", unique = true)
    private Grammar grammar;
}
