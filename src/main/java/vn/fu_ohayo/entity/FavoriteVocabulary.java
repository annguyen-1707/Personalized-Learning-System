package vn.fu_ohayo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Favorite_Vocabulary")
public class FavoriteVocabulary {

   @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    private Integer id;

    @Column(name = "is_public")
    private boolean isPublic = false;

    @Column(name = "name")
    private String name;

    @Column(name = "ownerName")
    private String ownerName;

    @ManyToMany(mappedBy = "favoriteVocabularies")
    private Set<User> users;


    @ManyToMany
    @JoinTable(
            name = "vocabulary_favorite_vocabulary",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
    )
    private Set<Vocabulary> vocabularies;

    @Column(name = "added_at")
    private Date addedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;



    @PrePersist
    protected void onAdd() {
        this.addedAt = new Date();
    }
}
