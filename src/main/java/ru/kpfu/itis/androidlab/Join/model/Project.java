package ru.kpfu.itis.androidlab.Join.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader", referencedColumnName = "id")
    private User leader;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Specialization> vacancies;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_project",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> participants;

    @OneToMany(mappedBy = "project")
    private List<Notification> notifications;


}
