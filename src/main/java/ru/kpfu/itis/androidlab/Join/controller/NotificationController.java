package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.form.AnswerNotificationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.NotificationServiceInt;

@RestController
@RequestMapping(value = "/notifications")
public class NotificationController {

    private NotificationServiceInt notificationService;

    public NotificationController(NotificationServiceInt notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity answerToNotification(@PathVariable Long id, @RequestBody AnswerNotificationForm answerForm) {
        notificationService.answerNotification(id, answerForm);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteNotification(@PathVariable Long id) {
        if (!notificationService.deleteNotification(id)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("error", "not allowed. please answer");
            ResponseEntity.status(400).headers(httpHeaders).build();
        }

        return ResponseEntity.ok().build();
    }

}
