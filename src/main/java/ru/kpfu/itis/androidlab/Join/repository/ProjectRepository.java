package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByParticipantsContainsOrLeader(User user, User user1);
    List<Project> findAllByLeader(User user);

    @Query(
            value = "SELECT * FROM project WHERE title LIKE %:searchTitle%",
            nativeQuery = true
    )
    public List<Project> searchProjectByTitle(@Param("searchTitle") String searchTitle);
}
