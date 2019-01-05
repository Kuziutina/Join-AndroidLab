package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.dto.NotificationDto;
import ru.kpfu.itis.androidlab.Join.form.AnswerNotificationForm;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface NotificationServiceInt {
    void addNotification(User user, Project project, int type);

    void answerNotification(Long id, AnswerNotificationForm answerNotificationForm);

    List<NotificationDto> getUserNotificationDtos(User user);

    List<User> getInvitedUser(Long projectId);

    List<User> getJoinedUser(Long projectId);

    List<Project> getProjectUserJoined(User user);

    List<Project> getProjectUserInvited(User user);
}
