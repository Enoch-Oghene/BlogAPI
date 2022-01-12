package ServiceImplementation;

import Repositories.Repository_Comment;
import Service.Service_Comment;
import domain.Comment;
import domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImplementation_Comment implements Service_Comment {
    private final Repository_Comment repository_comment;

    @Autowired
    public ServiceImplementation_Comment(Repository_Comment repository_comment) {
        this.repository_comment = repository_comment;
    }

    @Override
    public void saveComment(Comment comment) {
        repository_comment.save(comment);
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return repository_comment.findByCommentId(id);
    }

    @Override
    public List<Comment> getAllCommentsInPost(Post post) {
        return repository_comment.findByPost(post);
    }
}
