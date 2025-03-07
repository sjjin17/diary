package todaktodak.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.PostAndUserDto;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select new todaktodak.domain.post.dto.PostAndUserDto(p.id, u.username, p.writtenDate, p.title, p.emotion, p.isPublished) from Post p inner join User u on p.user.id = u.id where p.diary.id=(:diaryId) and p.writtenDate >= (:startDate) and p.writtenDate < (:endDate) and (p.user.id = :userId or p.isPublished = true)")
    List<PostAndUserDto> findPostsByDiaryIdAndMonth(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select new todaktodak.domain.post.dto.PostAndUserDto(p.id, u.username, p.writtenDate, p.title, p.emotion, p.isPublished) from Post p inner join User u on p.user.id = u.id where p.diary.id = (:diaryId) and p.writtenDate = (:writtenDate) and (p.user.id = (:userId) or p.isPublished = true)")
    List<PostAndUserDto> findPostsByDiaryIdAndWrittenDate(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("writtenDate") LocalDate writtenDate);

}
