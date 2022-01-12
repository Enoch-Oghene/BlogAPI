package Cntrollers;

import DTO.DTO_Response;
import DTO.DTO_User;
import Service.Service_User;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/blog/user")
public class Controller_User {
    private final Service_User service_user;


    @Autowired
    public Controller_User(Service_User service_user) {
        this.service_user = service_user;
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        DTO_User reg = service_user.addUser(user);

        if (reg.isStatus()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else{
            if (reg.getMessage().equals("This email has been registered by another user")) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @GetMapping
    public ResponseEntity<List> getAllUsers() {
        List<User> usersList = service_user.allUsers();

        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        Optional<User> optionalUser = service_user.getUserById(userId);

        return optionalUser
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/login")
    public ResponseEntity<DTO_User> loginUser(@Valid @RequestBody User user, HttpServletRequest request) {
        DTO_User response = service_user.logInUser(user);

        if (response.isStatus()) {
            HttpSession session = request.getSession();
            session.setAttribute("logged user", response.getData());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/delete/{Id}")
    public DTO_Response deleteUser (@PathVariable Long Id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("loggedUser");

        DTO_Response response = new DTO_Response();
        if (user == null) {
            response.setStatusCode(401);
            response.setMessage("user not logged in");
            return response;
        }

        Optional<User> optionalUser = service_user.getUserById(Id);

        if (optionalUser.isPresent() && optionalUser.get().getUserId().equals(user.getUserId())) {
            if (service_user.deleteUser(Id, user)) {
                response.setStatusCode(204);
                response.setMessage("account deletion pending");
            }
        } else {
            response.setStatusCode(401);
            response.setMessage("This account isn't yours");
        }
        return response;

    }

    @PostMapping("/reverseDelete/{Id}")
    public ResponseEntity<?> accountDeletionReverse (@PathVariable Long Id, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("loggedUser");

        DTO_Response response = new DTO_Response();
        if (user == null) {
            response.setStatusCode(401);
            response.setMessage("user not logged in");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String message = service_user.accountDeactivationReverse(user, Id);

        if (message.equals("successfully reversed")) {
            response.setStatusCode(202);
            response.setMessage(message);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        else {
            response.setMessage(message);

            if (message.equals("unauthorised user")) {
                response.setStatusCode(401);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            else if (message.equals("user not found")) {
                response.setStatusCode(404);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
