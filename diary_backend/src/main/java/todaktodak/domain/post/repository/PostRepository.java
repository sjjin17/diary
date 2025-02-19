package todaktodak.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todaktodak.domain.post.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.diary.id=(:diaryId) and year(p.writtenDate) = (:year) and month(p.writtenDate) = :month and (p.user.id = :userId or p.isPublished = false)")
    List<Post> findPostsByDiaryIdAndMonth(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("year") int year, @Param("month") int month);

}
