package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Recovery;
import ru.kpfu.itis.androidlab.Join.model.User;

public interface RecoveryRepository extends JpaRepository<Recovery, Long> {
    Recovery getByRecoveryLinkAndUser(String code, User user);

    Recovery getByUser(User user);
}
