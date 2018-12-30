package ru.kpfu.itis.androidlab.Join.helper;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
        JSONObject body = new JSONObject();
//        body.put("device", user.getDevice());
        body.put("priority", "high");

        JSONObject notification_body = new JSONObject();
        notification_body.put("title", notification.getType());
        notification_body.put("body", notification.getMessage());

        body.put("notification", notification);

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
