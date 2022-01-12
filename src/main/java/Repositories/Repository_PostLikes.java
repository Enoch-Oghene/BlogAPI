package Repositories;

import domain.Post;
import domain.PostLikes;
import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface Repository_PostLikes extends JpaRepository<PostLikes, Long> {
    Optional<PostLikes> findByPostAndUser(Post post, User user);
}
