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

//0 - пригласил  (кто пригласил, куда пригласил)
// 1 - согласился вступить (приходит группе)
// 2 - попросился  (кто попросился, куда попросился)
// 3 - приняли (приходит участнику, вас приняли)
// 4 - Отказался вступить (приходит группе)
// 5 - Отказались принимать (приходит участнику)
// 6 - Вы вступили (результат согласия)
// 7 - Вы отказались вступить (результат отказа)
// 8 - Вы приняли участника (результат принятия)
// 9 - Вы отказали участнику (результат отказа группы)
// 10 - Вас удалили из группы (дей. группа)
// 11 - Пользователь удалился из группы (действует пользователь)


@Component
public class NotificationHelper {

    private static final String INVITE = "Пользователь %s приглашает вас присоедениться к проекту %s";                  //0
    private static final String JOIN = "Пользователь %s хочет присоединиться к проекту %s";                             //2
    private static final String YES_FROM_USER = "Пользователь %s принял ваше приглашение на вступление в проект %s";    //1
    private static final String NO_FROM_USER = "Пользователь %s отклонил ваше приглашение на всупление в проект %s";    //1
    private static final String YES_FROM_PROJECT = "Пользователь %s принял вашу заявку на вступление в проект %s";      //3
    private static final String NO_FROM_PROJECT = "Пользователь %s отклонил вашу заявку на вступление в проект %s";
    private static final String DELETE_FROM_USER = "Пользователь %s вышел из группы %s";
    private static final String DELETE_FROM_PROJECT = "Вас Исключили из группы %s";

    private static final String TITLE_INVITE = "Приглашение в проект";
    private static final String TITLE_JOIN = "Заявка на вступление";
    private static final String TITLE_YES_FROM_USER = "Приглашение принято";
    private static final String TITLE_NO_FROM_USER = "Приглашение отклонено";
    private static final String TITLE_YES_FROM_PROJECT = "Заявка принята";
    private static final String TITLE_NO_FROM_PROJECT = "Заявка отклонена";
    private static final String TITLE_DELETE_FROM_USER = "Выход из группы";
    private static final String TITLE_DELETE_FROM_PROJECT = "Исключение из группы";

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
        String title;
        String body_notification;

        switch (notification.getType()){
            case 0: title = TITLE_INVITE;
                body_notification = String.format(INVITE, notification.getProject().getLeader().getUsername(), notification.getProject().getTitle());
                break;
            case 2: title = TITLE_JOIN;
                body_notification = String.format(JOIN, notification.getUser().getUsername(), notification.getProject().getTitle());
                break;
            case 1: title = TITLE_YES_FROM_USER;
                body_notification = String.format(YES_FROM_USER, notification.getUser().getUsername(), notification.getProject().getTitle());
                break;
            case 4: title = TITLE_NO_FROM_USER;
                body_notification = String.format(NO_FROM_USER, notification.getUser().getUsername(), notification.getProject().getTitle());
                break;
            case 3: title = TITLE_YES_FROM_PROJECT;
                body_notification = String.format(YES_FROM_PROJECT, notification.getProject().getLeader().getUsername(), notification.getProject().getTitle());
                break;
            case 5: title = TITLE_NO_FROM_PROJECT;
                body_notification = String.format(NO_FROM_PROJECT, notification.getProject().getLeader().getUsername(), notification.getProject().getTitle());
                break;
            case 11: title = TITLE_DELETE_FROM_USER;
                body_notification = String.format(DELETE_FROM_USER, notification.getUser().getUsername(), notification.getProject().getTitle());
                break;
            case 10: title = TITLE_DELETE_FROM_PROJECT;
                body_notification = String.format(DELETE_FROM_PROJECT, notification.getProject().getTitle());
                break;
                default: title = "error";
                body_notification = "";
                break;

        }

        notification_body.put("title", /*notification.getType()*/ title);
        notification_body.put("body", /*notification.getMessage()*/ body_notification);

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
        request.getHeaders().add("Content-type", "application/json; charset=UTF-8");
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
