package vn.fu_ohayo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fu_ohayo.entity.UserVocabulary;
@Repository
public interface UserVocabRepository extends JpaRepository<UserVocabulary, Integer> {
}
