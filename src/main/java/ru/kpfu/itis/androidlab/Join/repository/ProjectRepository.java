package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Project;

interface ProjectRepository extends JpaRepository<Project, Long> {
}
