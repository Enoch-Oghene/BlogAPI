package ServiceImplementation;

import Repositories.Repository_Followers;
import Repositories.Repository_User;
import Service.Service_Followers;
import domain.Followers;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ServiceImplementation_Followers implements Service_Followers {

    private final Repository_Followers repository_followers;
    private final Repository_User repository_user;

    @Autowired
    public ServiceImplementation_Followers(Repository_Followers repository_followers, Repository_User repository_user) {
        this.repository_followers = repository_followers;
        this.repository_user = repository_user;
    }

    @Override
    public String followUser (Long followeeId, User follower) {
        String response = "You're not able to make a connection";
       try {
           List<Followers> listOfFollowers = repository_followers.
                   findByFollowerIdAndAndFolloweeId(follower.getUserId(), followeeId);

           if (listOfFollowers.size() == 0) {
               Followers newFollower = new Followers();
               newFollower.setFolloweeId(followeeId);
               newFollower.setFollowerId(follower.getUserId());
               repository_followers.save(newFollower);
               response = "A new connection has been made by you";
           }else{
               repository_followers.deleteById(listOfFollowers.get(0).getId());
               response = "You have unfollowed the connection";
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return response;
    }


    @Override
    public List<User> getFollowersOfUser(User followee) {
        List<User> listOfFollowers = new ArrayList<>();

        try {
            List<Followers> list = repository_followers.findAllByFolloweeId(followee.getUserId());
            list.forEach(each ->{
                User g_user = new User();
                User follower = repository_user.findById(each.getFollowerId()).get();
                g_user.setUserId(follower.getUserId());
                g_user.setUserEmail(follower.getUserEmail());
                g_user.setUserPassword(follower.getUserPassword());
                listOfFollowers.add(g_user);

            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listOfFollowers;

    }

    @Override
    public List<User> getUsersFollowedByUser(User follower) {
        List<User> listOfFollowees = new ArrayList<>();

        try {
            List<Followers> data = repository_followers.findAllByFollowerId(follower.getUserId());
            data.forEach(each -> {
                User u_user = new User();
                User followee = repository_user.findById(each.getFolloweeId()).get();
                u_user.setUserId(followee.getUserId());
                u_user.setUserEmail(followee.getUserEmail());
                u_user.setUserPassword(followee.getUserPassword());
                listOfFollowees.add(u_user);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listOfFollowees;
    }


}
