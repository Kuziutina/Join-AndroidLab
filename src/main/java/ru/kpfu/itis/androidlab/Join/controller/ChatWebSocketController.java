package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.androidlab.Join.dto.MessageDto;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.service.MessageService;
import ru.kpfu.itis.androidlab.Join.service.UserService;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

@Controller
public class ChatWebSocketController {

    private UserServiceInt userService;
    private MessageService messageService;

    @Autowired
    public ChatWebSocketController(UserService userService,
                                   MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }


    @MessageMapping("/openApp/{userId}")
    public void notificationMessageOff(String m, @DestinationVariable Long userId) {
        User user = userService.getUser(userId);
        user.setOnline(true);
    }

    @MessageMapping("/chatGroup/{chatId}")
    @SendTo("/chatGroup/{chatId}")
    public MessageDto processMessage(@RequestBody MessageDto message, @DestinationVariable Long chatId) {

        message.setChatId(chatId);
        messageService.addMessage(message);

        return message;
    }

    @MessageMapping("/closeApp/{userId}")
    public void notificationMessageOn(String m, @DestinationVariable Long userId) {
        User user = userService.getUser(userId);
        user.setOnline(false);
    }
}
