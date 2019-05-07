package ru.kpfu.itis.androidlab.Join.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String text;
    private Date date;

    @ManyToOne()
    @JoinColumn(name = "chat", referencedColumnName = "id")
    private Chat chat;

    @ManyToOne()
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "receiver", referencedColumnName = "id")
    private User receiver;
}
