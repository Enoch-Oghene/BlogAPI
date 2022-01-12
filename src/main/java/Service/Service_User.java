package Service;

import DTO.DTO_User;
import domain.User;

import java.util.List;
import java.util.Optional;

public interface Service_User {
    DTO_User addUser(User user);
    DTO_User logInUser(User user);
    List<User> allUsers();

    Optional<User> getUserById(Long Id);

    void userDeactivationScheduler();

    boolean deleteUser(Long userId, User user);

    String accountDeactivationReverse(User user, Long userId);
}
