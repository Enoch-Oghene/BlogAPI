package ServiceImplementation;

import Repositories.Repository_Favourites;
import Service.Service_Favourites;
import domain.Favourites;
import domain.Post;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImplementation_Favourites implements Service_Favourites {

    private final Repository_Favourites repository_favourites;

    @Autowired
    public ServiceImplementation_Favourites(Repository_Favourites repository_favourites) {
        this.repository_favourites = repository_favourites;
    }

    @Override
    public Favourites getByPostAndUser(Post post, User user) {
        return repository_favourites.findByPostAndUser(post, user);
    }

    @Override
    public Favourites saveFavorite(Favourites favourite) {
        return repository_favourites.save(favourite);
    }

    @Override
    public List<Favourites> findAllByUser(User user) {
        return repository_favourites.findAllByUser(user);
    }

    @Override
    public void deleteFavorite(Favourites favorite) {
        repository_favourites.delete(favorite);
    }
}
