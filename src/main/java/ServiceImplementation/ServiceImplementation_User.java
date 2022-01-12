package ServiceImplementation;

import DTO.DTO_User;
import Repositories.Repository_User;
import Service.Service_User;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ServiceImplementation_User implements Service_User {

    @Autowired
    Repository_User repository_user;

    @Override
    public DTO_User addUser(User user) {
        DTO_User response = new DTO_User();

        try {
            List<String> domains = Arrays.asList("gmail.com", "yahoo.com", "hotmail.com");
            String[] email = user.getUserEmail().split("@");
            if (!domains.contains(email[1])) {
                throw new Exception("Email Not Valid");
            }
            Optional<User> userDB = repository_user.getUserByUserEmail(user.getUserEmail());

            try {
                if (userDB.isPresent()) {
                    throw new Exception("Email has been registered already");
                }

                User savedUser = repository_user.save(user);
                response.setData(savedUser);
                response.setMessage("Congratulations! You've been registered Successfully");
                response.setStatus(true);
                return response;

            } catch (Exception e) {
                response.setMessage(e.getMessage());
                response.setStatus(false);
                return response;
            }

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(false);
            return response;
        }
    }


    public DTO_User logInUser(User user) {
        Optional<User> userDB = repository_user.getUserByUserEmailAndUserPassword
                (user.getUserEmail(), user.getUserPassword());
        DTO_User response = new DTO_User();

        if (userDB.isPresent()) {
            response.setStatus(true);
            response.setData(userDB.get());
            response.setMessage("You've successfully logged in");
            return response;
        }
        response.setMessage("Invalid Mail or Password");
        return response;
    }


    @Override
    public List<User> allUsers() {
        return repository_user.findAll();
    }


    @Override
    public Optional<User> getUserById(Long Id) {
        return repository_user.findById(Id);
    }

    @Override
    public void userDeactivationScheduler() {
        List<User> users = repository_user.findAllByPersonDeactivated(1);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd  ss:mm:hh");
        System.out.println("Scheduler Working Fine");

        users.forEach(user -> {
            String currentDate = dateFormat.format(date);
            String deleteDate = user.getRemoveDate();
            int deleteAction = currentDate.compareTo(deleteDate);

            if (deleteAction > 0 || deleteAction == 0) {
                System.out.println(user.getUserEmail() + " user completed deleted");
                user.setIsDelete(1);
                repository_user.save(user);
            }
        });
    }


    @Override
    public boolean deleteUser(Long userId, User user) {
        boolean status = false;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd ss:mm:hh");

        try {
            User d_user = repository_user.getUserByUserEmail(user.getUserEmail()).get();
            if (d_user.getUserId() == userId) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, 1);
                String currentDate = dateFormat.format(cal.getTime());
                d_user.setPersonDeactivated(1);
                d_user.setRemoveDate(currentDate);
                repository_user.save(d_user);
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }



    @Override
    public String accountDeactivationReverse(User user, Long userId) {
        String status = "Error";

        try {
            User r_user = repository_user.getUserByUserEmail(user.getUserEmail()).get();
            if (r_user.getUserId() != userId) status = "unauthorized user";
            else {
                if (r_user.getIsDelete() == 0) {
                    r_user.setPersonDeactivated(0);
                    repository_user.save(r_user);
                    status = "reversed successfully";
                } else {
                    status = "user not found";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


}
