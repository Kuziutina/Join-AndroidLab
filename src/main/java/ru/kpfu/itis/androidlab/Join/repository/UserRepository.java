package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserByUsername(String username);

    @Query(
            value = "SELECT * FROM _user WHERE username LIKE %:searchUsername%",
            nativeQuery = true
    )
    List<User> searchUsersByUsername(@Param("searchUsername") String searchUsername);
}
