package ru.kpfu.itis.androidlab.Join.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Notification {

    // 0 - пригласил
    // 1 - согласился/отказался
    // 2 - попросился
    // 3 - приняли/отклонили
    // 4 - otkaz
    // 5 - otklon

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    private String message;

    private Integer type;

    private Date date;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean seeing;
}
