package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Confirmation;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    void deleteByEmail(String email);
    Confirmation findByEmail(String email);
}
