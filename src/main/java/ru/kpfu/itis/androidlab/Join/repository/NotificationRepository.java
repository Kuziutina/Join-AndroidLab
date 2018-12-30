package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //TODO optimization
    List<Notification> findAllByTypeInAndUser(List<Integer> type, User user);
    List<Notification> findAllByTypeInAndProjectIn(List<Integer> type, List<Project> projects);
}
