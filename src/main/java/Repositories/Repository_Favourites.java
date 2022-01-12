package Repositories;

import domain.Favourites;
import domain.Post;
import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repository_Favourites extends JpaRepository<Favourites, Long> {
    Favourites findByPostAndUser(Post post, User user);

    List<Favourites> findAllByUser(User user);
}
