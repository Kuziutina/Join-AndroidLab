package ru.kpfu.itis.androidlab.Join.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private String name;

    private String lastName;

    private String phone;

    private String profileImageLink;

    private String tokenDevice;

    private Boolean online;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<Project> projects;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Recovery recovery;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Specialization> specializations;

    @OneToMany(mappedBy = "leader", fetch = FetchType.LAZY)
    private List<Project> leaderProjects;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Chat> chats_sender;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Chat> chats_receiver;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, email, username, password, name, lastName, phone);
    }

    public List<Chat> getChat() {
        List<Chat> chats = new ArrayList<>();
        chats.addAll(chats_receiver);
        chats.addAll(chats_sender);
        return chats;
    }
}
