package Repositories;

import domain.Comment;
import domain.CommentLikes;
import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface Repository_CommentLikes extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByCommentAndUser(Comment comment, User user);
}
