package ServiceImplementation;

import Repositories.Repository_PostLikes;
import Service.Service_PostLikes;
import domain.Post;
import domain.PostLikes;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ServiceImplementation_PostLikes implements Service_PostLikes {

    @Autowired
    Repository_PostLikes repository_postLikes;

    @Override
    public void likePost(PostLikes like) {
        repository_postLikes.save(like);
    }

    @Override
    public void deletePostLike(PostLikes like) {
        repository_postLikes.delete(like);
    }

    @Override
    public Optional<PostLikes> findPostLike(Post post, User user) {
        return repository_postLikes.findByPostAndUser(post, user);
    }
}
