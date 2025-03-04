package todaktodak.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todaktodak.domain.post.domain.Post;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.diary.id=(:diaryId) and p.writtenDate >= (:startDate) and p.writtenDate < (:endDate) and (p.user.id = :userId or p.isPublished = true)")
    List<Post> findPostsByDiaryIdAndMonth(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select p from Post p where p.diary.id = (:diaryId) and p.writtenDate = (:writtenDate) and (p.user.id = (:userId) or p.isPublished = true)")
    List<Post> findPostsByDiaryIdAndWrittenDate(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("writtenDate") LocalDate writtenDate);

}
