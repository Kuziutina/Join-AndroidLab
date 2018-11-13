package ru.kpfu.itis.androidlab.Join.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "knowledge_level")
    private Integer knowledgeLevel;

    private Integer experience;

    private String technologies;

    @ManyToOne
    @JoinColumn(name = "specialization_name_id", referencedColumnName = "id")
    private SpecializationName specializationName;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
