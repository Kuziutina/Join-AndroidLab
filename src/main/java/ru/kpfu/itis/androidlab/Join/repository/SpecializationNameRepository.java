package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.SpecializationName;

public interface SpecializationNameRepository extends JpaRepository<SpecializationName, Long> {
    SpecializationName findByNameIgnoreCase(String name);
}
