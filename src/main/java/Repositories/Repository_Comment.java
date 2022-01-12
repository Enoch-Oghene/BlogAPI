package Repositories;

import domain.Comment;
import domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_Comment extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentId(Long id);
    List<Comment> findByPost(Post post);
}
