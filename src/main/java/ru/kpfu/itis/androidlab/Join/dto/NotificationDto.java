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
    SimpleProjectDto project;
    SimpleUserDto user;
    Integer type;
    Date date;
    boolean seeing;

    private NotificationDto(Notification notification) {
        this.id = notification.getId();
//        this.message = notification.getMessage();
        this.type = notification.getType();

        this.project = SimpleProjectDto.from(notification.getProject());

        if (type == 0 || type == 3 || type == 5) {
            this.user = SimpleUserDto.from(notification.getProject().getLeader());
        }
        else {
            this.user = SimpleUserDto.from(notification.getUser());
        }
        this.date = notification.getDate();
        this.seeing = notification.isSeeing();
    }

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(notification);
    }
}
