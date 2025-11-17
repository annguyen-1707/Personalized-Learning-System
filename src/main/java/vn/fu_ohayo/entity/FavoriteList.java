package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Favorite_List")
public class FavoriteList {

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "favorite_list_id")
    private int favoriteId;

    @Column(name = "favorite_list_name")
    private String favoriteListName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_public")
    private boolean isPublic = false;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "favoriteList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FavoriteListVocabulary> favoriteListVocabularies = new HashSet<>();

    @OneToMany(mappedBy = "favoriteList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FavoriteListGrammar> favoriteListGrammars = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
