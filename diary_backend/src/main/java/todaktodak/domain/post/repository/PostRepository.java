package todaktodak.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.post.dto.PostAndPostLikeDto;
import todaktodak.domain.post.dto.PostAndUserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select new todaktodak.domain.post.dto.PostAndUserDto(p.id, u.username, p.writtenDate, p.title, p.emotion, p.isPublished) from Post p inner join User u on p.user.id = u.id where p.diary.id=(:diaryId) and p.writtenDate >= (:startDate) and p.writtenDate < (:endDate) and (p.user.id = :userId or p.isPublished = true)")
    List<PostAndUserDto> findPostsByDiaryIdAndMonth(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select new todaktodak.domain.post.dto.PostAndUserDto(p.id, u.username, p.writtenDate, p.title, p.emotion, p.isPublished) from Post p inner join User u on p.user.id = u.id where p.diary.id = (:diaryId) and p.writtenDate = (:writtenDate) and (p.user.id = (:userId) or p.isPublished = true)")
    List<PostAndUserDto> findPostsByDiaryIdAndWrittenDate(@Param("userId") Long userId, @Param("diaryId") Long diaryId, @Param("writtenDate") LocalDate writtenDate);

    @Query("select new todaktodak.domain.post.dto.PostAndPostLikeDto(p.id, p.writtenDate, p.title, p.content, p.weather, p.emotion, p.isPublished, count(pl.postLikeId)) from Post  p left outer join PostLike pl on p.id = pl.post.id where p.id = (:postId) group by p.id")
    Optional<PostAndPostLikeDto> findPostAndLikeById(@Param("postId") Long postId);
}
