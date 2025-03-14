package todaktodak.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todaktodak.domain.post.domain.PostLike;
import todaktodak.domain.user.domain.User;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsByPostIdAndUserId(Long postId, Long userId);

    @Modifying(clearAutomatically = true)
    @Query("delete from PostLike pl where pl.post.id = (:postId) and pl.user.id = (:userId)")
    void deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    Long countByPostId(Long postId);
}