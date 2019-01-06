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

// 0 - пригласил  (кто пригласил, куда пригласил)
// 1 - согласился вступить (приходит группе)
// 2 - попросился  (кто попросился, куда попросился)
// 3 - приняли (приходит участнику, вас приняли)
// 4 - Отказался вступить (приходит группе)
// 5 - Отказались принимать (приходит участнику)
// 6 - Вы вступили (результат согласия)
// 7 - Вы отказались вступить (результат отказа)
// 8 - Вы приняли участника (результат принятия)
// 9 - Вы отказали участнику (результат отказа группы)
// 10 - Вас удалили из группы (дей. группа)
// 11 - Пользователь удалился из группы (действует пользователь)


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

//    private String message;

    private Integer type;

    private Date date;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean seeing;
}
