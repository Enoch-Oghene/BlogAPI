package Cntrollers;


import Service.Service_Followers;
import Service.Service_Post;
import Service.Service_PostLikes;
import domain.Post;
import domain.PostLikes;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog/posts")
public class Controller_Post {
    private final Service_Followers service_followers;
    private final Service_Post service_post;
    private final Service_PostLikes service_Likes;

    @Autowired
    public Controller_Post(Service_Followers service_followers,
                           Service_Post service_post, Service_PostLikes service_Likes) {
        this.service_followers = service_followers;
        this.service_post = service_post;
        this.service_Likes = service_Likes;
    }

    @GetMapping()
    public ResponseEntity<List> getAllPosts(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");

        if (user != null) {
            List<Post> listOfPosts = service_post.allPosts();
            return new ResponseEntity<>(listOfPosts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PostMapping()
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            service_post.addPost(user, post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/followees")
    public ResponseEntity<List> getPostsOfConnections(HttpSession httpSession){
        User user = (User) httpSession.getAttribute("loggedUser");
        List<User> listOfFollowees = service_followers.getUsersFollowedByUser(user);

        if (listOfFollowees.size() != 0) {
            List<Post> listOfPosts = service_post.allPosts();
            List<Post> listOfPostsOfConnections = new ArrayList<>();

            for (Post post: listOfPosts){
                for (User p_user: listOfFollowees) {
                    if (post.getUser().equals(p_user))
                        listOfPostsOfConnections.add(post);

                }
            }
            return new ResponseEntity<>(listOfPostsOfConnections, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/likes/{postId}")
    public ResponseEntity<PostLikes> likeAndUnlikePost(@Valid PostLikes postLike, @PathVariable Long postId,
                                                       HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Optional<Post> optionalPost = service_post.getPostById(postId);

        if (user != null && optionalPost.isPresent()) {
            Optional<PostLikes> optionalPostLikes = service_Likes.findPostLike(optionalPost.get(), user);

            if (optionalPostLikes.isPresent()) {
                PostLikes postLikes = optionalPostLikes.get();

                if (postLikes.getUser().getUserId().equals(user.getUserId()))
                    service_Likes.deletePostLike(postLikes);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            postLike.setPost(optionalPost.get());
            postLike.setUser(user);

            service_Likes.likePost(postLike);
            return new ResponseEntity<>(postLike, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
