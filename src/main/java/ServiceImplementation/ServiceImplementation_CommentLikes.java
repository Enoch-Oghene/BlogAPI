package ServiceImplementation;


import Repositories.Repository_CommentLikes;
import Service.Service_CommentLikes;
import domain.Comment;
import domain.CommentLikes;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ServiceImplementation_CommentLikes implements Service_CommentLikes {

    @Autowired
    Repository_CommentLikes repository_commentLikes;

    @Override
    public void likeComment(CommentLikes like) {
        repository_commentLikes.save(like);
    }

    @Override
    public void deleteCommentLike(CommentLikes like) {
        repository_commentLikes.delete(like);
    }

    @Override
    public Optional<CommentLikes> findCommentLike(Comment comment, User user) {
        return repository_commentLikes.findByCommentAndUser(comment, user);

    }
}
