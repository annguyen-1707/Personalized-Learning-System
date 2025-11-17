package vn.fu_ohayo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fu_ohayo.entity.FavoriteList;

public interface FavoriteListRepository extends JpaRepository<FavoriteList, Long> {

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email = :email
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND SIZE(f.favoriteListVocabularies) > 0
          AND SIZE(f.favoriteListGrammars) = 0
    """)
    Page<FavoriteList> findVocabularyFolders(
            @Param("email") String email,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email = :email
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND SIZE(f.favoriteListGrammars) > 0
          AND SIZE(f.favoriteListVocabularies) = 0
    """)
    Page<FavoriteList> findGrammarFolders(
            @Param("email") String email,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email = :email
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<FavoriteList> findAllFolders(
            @Param("email") String email,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email <> :email
          AND f.isPublic = true
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<FavoriteList> findPublicFoldersExcludeUser(
            @Param("email") String email,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email <> :email
          AND f.isPublic = true
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND SIZE(f.favoriteListVocabularies) > 0
          AND SIZE(f.favoriteListGrammars) = 0
    """)
    Page<FavoriteList> findPublicVocabularyFoldersExcludeUser(
            @Param("email") String email,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email <> :email
          AND f.isPublic = true
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND SIZE(f.favoriteListGrammars) > 0
          AND SIZE(f.favoriteListVocabularies) = 0    
    """)
    Page<FavoriteList> findPublicGrammarFoldersExcludeUser(
            @Param("email") String email,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email = :email
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND f.isPublic = :isPublic
          AND SIZE(f.favoriteListVocabularies) > 0
          AND SIZE(f.favoriteListGrammars) = 0
    """)
    Page<FavoriteList> findMyVocabularyFoldersWithPublic(
            @Param("email") String email,
            @Param("keyword") String keyword,
            @Param("isPublic") boolean isPublic,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email = :email
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND f.isPublic = :isPublic
          AND SIZE(f.favoriteListGrammars) > 0
          AND SIZE(f.favoriteListVocabularies) = 0
    """)
    Page<FavoriteList> findMyGrammarFoldersWithPublic(
            @Param("email") String email,
            @Param("keyword") String keyword,
            @Param("isPublic") boolean isPublic,
            Pageable pageable
    );

    @Query("""
        SELECT f FROM FavoriteList f
        WHERE f.user.email = :email
          AND LOWER(f.favoriteListName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          AND f.isPublic = :isPublic
    """)
    Page<FavoriteList> findMyAllFoldersWithPublic(
            @Param("email") String email,
            @Param("keyword") String keyword,
            @Param("isPublic") boolean isPublic,
            Pageable pageable
    );

    // === ĐẾM SỐ MỤC ===

    @Query("""
        SELECT COUNT(fv) 
        FROM FavoriteListVocabulary fv 
        WHERE fv.favoriteListId = :id
    """)
    long countVocabularies(@Param("id") long favoriteListId);

    @Query("""
        SELECT COUNT(fg) 
        FROM FavoriteListGrammar fg 
        WHERE fg.favoriteListId = :id
    """)
    long countGrammars(@Param("id") long favoriteListId);

}
