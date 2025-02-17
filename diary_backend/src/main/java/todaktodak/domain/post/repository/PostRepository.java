package todaktodak.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todaktodak.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
