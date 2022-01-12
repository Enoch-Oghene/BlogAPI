package Cntrollers;

import Service.Service_Comment;
import Service.Service_CommentLikes;
import Service.Service_Post;
import domain.Comment;
import domain.CommentLikes;
import domain.Post;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog/comments")
public class Controller_Comment {
    private final Service_Post service_post;
    private final Service_Comment service_comment;
    private final Service_CommentLikes service_commentLikes;


    @Autowired
    public Controller_Comment(Service_Post service_post,
                              Service_Comment service_comment, Service_CommentLikes service_commentLikes) {
        this.service_post = service_post;
        this.service_comment = service_comment;
        this.service_commentLikes = service_commentLikes;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment,
                                                 @PathVariable Long postId,
                                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Optional<Post> optionalPost = service_post.getPostById(postId);


        if (user != null && optionalPost.isPresent()) {
            comment.setUser(user);
            optionalPost.ifPresent(comment::setPost);
            service_comment.saveComment(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List> getAllCommentsInPost(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Optional<Post> optionalPost = service_post.getPostById(postId);

        if (user != null && optionalPost.isPresent()) {
            List<Comment> listOfComments = service_comment.getAllCommentsInPost(optionalPost.get());
            return new ResponseEntity<>(listOfComments, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/commentlikes/{commentId}")
    public ResponseEntity<CommentLikes> likeAndUnlikeComment(@Valid CommentLikes commentLike,
                                                             @PathVariable Long commentId,
                                                             HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Optional<Comment> optionalComment = service_comment.getCommentById(commentId);
        if (user != null && optionalComment.isPresent()) {
            Optional<CommentLikes> optionalCommentLikes = service_commentLikes.findCommentLike(optionalComment.get(), user);

            if (optionalCommentLikes.isPresent()) {
                CommentLikes commentLikes = optionalCommentLikes.get();

                if (commentLikes.getUser().getUserId().equals(user.getUserId()))
                    service_commentLikes.deleteCommentLike(commentLikes);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            commentLike.setComment(optionalComment.get());
            commentLike.setUser(user);

            service_commentLikes.likeComment(commentLike);
            return new ResponseEntity<>(commentLike, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
