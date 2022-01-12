package Service;

import domain.Comment;
import domain.Post;

import java.util.List;
import java.util.Optional;

public interface Service_Comment {
    void saveComment(Comment comment);
    Optional<Comment> getCommentById(Long id);
    List<Comment> getAllCommentsInPost(Post post);
}
