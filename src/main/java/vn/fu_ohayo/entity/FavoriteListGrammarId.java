package vn.fu_ohayo.entity;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteListGrammarId implements Serializable {
    private int favoriteListId;
    private int grammarId;

    public FavoriteListGrammarId() {}
    public FavoriteListGrammarId(int favoriteListId, int grammarId) {
        this.favoriteListId = favoriteListId;
        this.grammarId = grammarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteListGrammarId)) return false;
        FavoriteListGrammarId that = (FavoriteListGrammarId) o;
        return favoriteListId == that.favoriteListId && grammarId == that.grammarId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(favoriteListId, grammarId);
    }
}
