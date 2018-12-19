package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kpfu.itis.androidlab.Join.form.AnswerNotificationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.NotificationServiceInt;

@Controller(value = "/notification")
public class NotificationController {

    private NotificationServiceInt notificationService;

    public NotificationController(NotificationServiceInt notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity answerToNotification(@PathVariable Long id, AnswerNotificationForm answerForm) {
        return ResponseEntity.ok().build();
    }

}
