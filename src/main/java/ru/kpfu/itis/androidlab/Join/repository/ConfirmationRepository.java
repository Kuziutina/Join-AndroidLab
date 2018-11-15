package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Confirmation;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    void deleteAllByEmail(String email);
    Confirmation findOneByEmail(String email);
}
