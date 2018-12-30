package ru.kpfu.itis.androidlab.Join.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kpfu.itis.androidlab.Join.model.Notification;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    Long id;
    String message;
    Long projectId;
    Long userId;
    Integer type;
    Date date;
    boolean seeing;

    private NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.projectId = notification.getProject().getId();
        this.userId = notification.getUser().getId();
        this.type = notification.getType();
        this.date = notification.getDate();
        this.seeing = notification.isSeeing();
    }

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(notification);
    }
}
