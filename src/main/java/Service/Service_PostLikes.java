package Service;

import domain.Post;
import domain.PostLikes;
import domain.User;

import java.util.Optional;

public interface Service_PostLikes {
    void likePost(PostLikes like);
    void deletePostLike(PostLikes like);
    Optional<PostLikes> findPostLike(Post post, User user);
}
