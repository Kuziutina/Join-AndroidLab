package ru.kpfu.itis.androidlab.Join.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.UserDto;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class NotificationHelper {

    PushNotificationsService pushNotificationsService;

    public NotificationHelper(PushNotificationsService pushNotificationsService) {
        this.pushNotificationsService = pushNotificationsService;
    }

    public ResponseEntity<String> send(User user, Notification notification) {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject body = new JSONObject();
        body.put("to", user.getTokenDevice());
        body.put("priority", "high");

        JSONObject notification_body = new JSONObject();
        notification_body.put("title", /*notification.getType()*/ "testInvite");
        notification_body.put("body", /*notification.getMessage()*/ "boooody");

        body.put("notification", notification_body);
        JSONObject data = new JSONObject();

        data.put("id", notification.getId());
        data.put("type", notification.getType());
        try {
            data.put("user", objectMapper.writeValueAsString(UserDto.from(notification.getUser())));
            data.put("project", objectMapper.writeValueAsString(ProjectDto.from(notification.getProject())));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        body.put("data", data);
        HttpEntity<String> request = new HttpEntity<>(body.toString());
        CompletableFuture<String> pushNotification = pushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>("ERROR NOTIFICATION", HttpStatus.BAD_REQUEST);
    }
}
