package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.form.AnswerNotificationForm;
import ru.kpfu.itis.androidlab.Join.model.Notification;

public interface NotificationServiceInt {
    void addNotification(Notification notification);
    void answerNotification(Long id, AnswerNotificationForm answerNotificationForm);
}
