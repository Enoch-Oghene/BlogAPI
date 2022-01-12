package Repositories;

import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_User extends JpaRepository<User, Long> {
    Optional<User> getUserByUserEmail(String userEmail);
    Optional<User> getUserByUserEmailAndUserPassword(String userEmail, String userPassword);

    List<User> findAllByPersonDeactivated(int number);
        }
