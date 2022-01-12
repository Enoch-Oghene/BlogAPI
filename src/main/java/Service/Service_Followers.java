package Service;

import domain.User;

import java.util.List;

public interface Service_Followers {
    String followUser(Long followeeId, User follower);
    List<User> getFollowersOfUser(User user);
    List<User> getUsersFollowedByUser(User user);
}
