package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.Specialization;
import ru.kpfu.itis.androidlab.Join.model.SpecializationName;
import ru.kpfu.itis.androidlab.Join.model.User;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Specialization findBySpecializationNameAndUser(SpecializationName name, User user);
    Specialization findBySpecializationNameAndProject(SpecializationName name, Project project);
    void deleteAllByProject(Project project);
    void deleteSpecializationsByProject(Project project);
    void deleteSpecializationsByUser(User user);
}
