package Service;

import domain.Comment;
import domain.CommentLikes;
import domain.User;

import java.util.Optional;

public interface Service_CommentLikes {
    void likeComment(CommentLikes like);
    void deleteCommentLike(CommentLikes like);
    Optional<CommentLikes> findCommentLike(Comment comment, User user);
}
