package ru.kpfu.itis.androidlab.Join.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;

    private String username;

    private String password;

    private String token;

    private String name;

    private String lastName;

    private String phone;

    private boolean confirmed;

    private String code;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<Project> projects;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Recovery recovery;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Specialization> specializations;

}
