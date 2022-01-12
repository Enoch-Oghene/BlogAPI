package Service;

import domain.Post;
import domain.User;

import java.util.List;
import java.util.Optional;

public interface Service_Post {
    void addPost(User user, Post post);
    List<Post> getAllPostsByUser(User user);
    void updatePost(Post post);
    void deletePost(Post post);
    Optional<Post> getPostById(Long id);
    List<Post> allPosts();
}
