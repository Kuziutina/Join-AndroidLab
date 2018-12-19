package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.form.AnswerNotificationForm;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.repository.NotificationRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.NotificationServiceInt;

@Service
public class NotificationServiceImpl implements NotificationServiceInt {

    private NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void addNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void answerNotification(Long id, AnswerNotificationForm answerNotificationForm) {
        Notification notification = notificationRepository.getOne(id);
        //TODO i stop here
    }
}
