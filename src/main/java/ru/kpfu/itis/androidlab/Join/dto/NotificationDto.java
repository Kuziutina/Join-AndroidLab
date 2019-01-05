package ru.kpfu.itis.androidlab.Join.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    Long id;
//    String message;
    Project project;
    User user;
    Integer type;
    Date date;
    boolean seeing;

    private NotificationDto(Notification notification) {
        this.id = notification.getId();
//        this.message = notification.getMessage();
        this.type = notification.getType();

        this.project = new Project();
        this.project.setId(notification.getProject().getId());
        this.project.setTitle(notification.getProject().getTitle());

        this.user = new User();
        if (type == 0 || type == 3 || type == 5) {
            this.user.setId(notification.getProject().getLeader().getId());
            this.user.setUsername(notification.getProject().getLeader().getUsername());
        }
        else {
            this.user.setId(notification.getUser().getId());
            this.user.setUsername(notification.getUser().getUsername());
        }
        this.date = notification.getDate();
        this.seeing = notification.isSeeing();
    }

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(notification);
    }
}
