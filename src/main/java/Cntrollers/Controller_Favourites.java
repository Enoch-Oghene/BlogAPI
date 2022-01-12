package Cntrollers;

import Service.Service_Favourites;
import Service.Service_Post;
import Service.Service_User;
import domain.Favourites;
import domain.Post;
import domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog/stars")
public class Controller_Favourites {
    Service_Post service_post;
    Service_Favourites service_favourites;
    Service_User service_user;

    public Controller_Favourites(Service_Post service_post, Service_Favourites service_favourites, Service_User service_user) {
        this.service_post = service_post;
        this.service_favourites = service_favourites;
        this.service_user = service_user;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Favourites> addPostToFavorite(@PathVariable(name = "postId") Long postId,
                                                        HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            Optional<Post> post = service_post.getPostById(postId);
            if (post.isPresent()) {
                Favourites favourites = service_favourites.getByPostAndUser(post.get(), user);
                if (favourites == null) {
                    Favourites a_favourites = new Favourites();
                    a_favourites.setUser(user);
                    a_favourites.setPost(post.get());
                    Favourites b_favourites = service_favourites.saveFavorite(a_favourites);
                    return new ResponseEntity<>(b_favourites, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping()
    public ResponseEntity<List> getAllFavouritePosts(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");

        if (user != null) {
            List<Favourites> listOfFavourites = service_favourites.findAllByUser(user);
            List<Post> listOfPosts = new ArrayList<>();
            for (Favourites favourites : listOfFavourites)
                listOfPosts.add(favourites.getPost());
            return new ResponseEntity<>(listOfPosts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePostFromFavorite (HttpSession session, @PathVariable(name = "postId") Long postId) {
        User user = (User) session.getAttribute("loggedUser");
        Optional<Post> post = service_post.getPostById(postId);


        if (user != null && post.isPresent()) {
            Favourites favourites = service_favourites.getByPostAndUser(post.get(), user);

            if (favourites != null) {
                service_favourites.deleteFavorite(favourites);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


}
