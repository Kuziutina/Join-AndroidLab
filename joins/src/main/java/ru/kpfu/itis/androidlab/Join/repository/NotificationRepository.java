package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
