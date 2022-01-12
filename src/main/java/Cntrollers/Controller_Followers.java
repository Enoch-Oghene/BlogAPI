package Cntrollers;


import DTO.DTO_Response;
import Service.Service_Followers;
import Service.Service_User;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
public class Controller_Followers {

    private final Service_Followers service_followers;
    private final Service_User service_user;

    @Autowired
    public Controller_Followers(Service_Followers service_followers, Service_User service_user) {
        this.service_followers = service_followers;
        this.service_user = service_user;
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<DTO_Response> followUser(@PathVariable Long id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("loggedUser");

        DTO_Response response = new DTO_Response();
        if (user == null) {
            response.setStatusCode(401);
            response.setMessage("user not logged in!!!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Optional<User> optionalUser = service_user.getUserById(id);

        if (!user.getUserId().equals(id) && optionalUser.isPresent()) {
            String message = service_followers.followUser(id, user);

            if(message.equals("You have made a new connection") || message.equals("You have unfollowed a connection")) {
                response.setStatusCode(201);
                response.setMessage(message);
                return new ResponseEntity<>(response, HttpStatus.OK);

            }
        }else if (user.getUserId().equals(id)) {
            response.setStatusCode(301);
            response.setMessage("Cannot Follow Self");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        response.setStatusCode(500);
        response.setMessage("Server Error");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("loggedUser");

        DTO_Response response = new DTO_Response();
        var data = service_followers.getFollowersOfUser(user);
        if(user == null){
            response.setStatusCode(401);
            response.setMessage("user not logged in!!!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(service_followers.getFollowersOfUser(user), HttpStatus.OK);
    }

    @GetMapping("/followees")
    public ResponseEntity<?> getUsersFollowedByUser(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("loggedUser");

        DTO_Response response = new DTO_Response();

        if(user == null) {
            response.setStatusCode(401);
            response.setMessage("user not logged in");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(service_followers.getUsersFollowedByUser(user), HttpStatus.OK);
    }
}
