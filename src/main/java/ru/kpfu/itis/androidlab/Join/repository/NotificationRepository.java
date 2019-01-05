package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //TODO optimization
    List<Notification> findAllByTypeInAndUser(List<Integer> type, User user);
    List<Notification> findAllByTypeInAndProjectIn(List<Integer> type, List<Project> projects);

//    @Query("select n from Notification n where n.type = ?1 and n.project ")
    List<Notification> findAllByTypeAndProject(Integer type, Project project);

    List<Notification> findAllByTypeAndUser(Integer type, User user);
}
