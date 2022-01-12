package ServiceImplementation;

import Repositories.Repository_Comment;
import Repositories.Repository_Post;
import Repositories.Repository_PostLikes;
import Service.Service_Post;
import domain.Post;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImplementation_Post implements Service_Post {

    @Autowired
    Repository_Post repository_post;

    @Autowired
    Repository_PostLikes repository_Likes;

    @Autowired
    Repository_Comment repository_comment;

    public void addPost(User user, Post post) {
        post.setUser(user);
        repository_post.save(post);
    }

    @Override
    public List<Post> getAllPostsByUser(User user) {
        return repository_post.findAllByUser(user);
    }

    @Override
    public void updatePost(Post post) {
        repository_post.save(post);
    }

    @Override
    public void deletePost(Post post) {
        repository_post.delete(post);
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return repository_post.findByPostId(id);
    }

    @Override
    public List<Post> allPosts() {
        return repository_post.findAll();
    }
}
