package vn.fu_ohayo.entity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.fu_ohayo.enums.ContentTypeEnum;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.Set;

@Entity
@Table(name = "Contents")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude ={ "contents", "contentListening", "ContentReading", "contentSpeaking"})
public class Content {
    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "content_id")
    private long contentId;

    @NotNull(message = ErrorEnum.NOT_EMPTY_CONTENT_TYPE)
    @Enumerated(EnumType.STRING)
    private ContentTypeEnum contentType;


    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProgressContent> contents;

    @OneToOne(mappedBy = "content")
    private ContentListening contentListening;

    @OneToOne(mappedBy = "content")
    private ContentReading ContentReading;

    @OneToOne(mappedBy = "content")
    private ContentSpeaking contentSpeaking;

}
