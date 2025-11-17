package vn.fu_ohayo.entity;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteListVocabularyId implements Serializable {
    private int favoriteListId;
    private int vocabularyId;

    public FavoriteListVocabularyId() {}
    public FavoriteListVocabularyId(int favoriteListId, int vocabularyId) {
        this.favoriteListId = favoriteListId;
        this.vocabularyId = vocabularyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteListVocabularyId)) return false;
        FavoriteListVocabularyId that = (FavoriteListVocabularyId) o;
        return favoriteListId == that.favoriteListId && vocabularyId == that.vocabularyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(favoriteListId, vocabularyId);
    }
}
