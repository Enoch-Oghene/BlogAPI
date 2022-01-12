package Repositories;

import domain.Post;
import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_Post extends JpaRepository<Post, Long> {
    Optional<Post> findByPostId(Long id);
    List<Post> findAllByUser(User user);
}
