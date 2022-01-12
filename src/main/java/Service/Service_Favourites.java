package Service;

import domain.Favourites;
import domain.Post;
import domain.User;

import java.util.List;

public interface Service_Favourites {
    Favourites getByPostAndUser(Post post, User user);

    Favourites saveFavorite(Favourites favourite);

    List<Favourites> findAllByUser(User user);

    void deleteFavorite(Favourites favorite);
}
