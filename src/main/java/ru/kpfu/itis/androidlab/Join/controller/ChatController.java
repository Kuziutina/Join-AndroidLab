package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.details.CustomUserDetails;
import ru.kpfu.itis.androidlab.Join.dto.ChatDto;
import ru.kpfu.itis.androidlab.Join.dto.MessageDto;
import ru.kpfu.itis.androidlab.Join.model.Message;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.service.MessageService;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ChatServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.MessageServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.util.List;

@RestController
public class ChatController {

    private UserServiceInt userService;
    private MessageServiceInt messageService;
    private ChatServiceInt chatService;

    @Autowired
    public ChatController(UserServiceInt userService,
                          MessageServiceInt messageService,
                          ChatServiceInt chatService) {
        this.userService = userService;
        this.messageService = messageService;
        this.chatService = chatService;
    }


    @GetMapping(value = "/chat")
    public List<ChatDto> getAllUsersChat(Authentication authentication) {

        //TODO how its work??

        Long userId = ((User)authentication.getPrincipal()).getId();

        return chatService.getUsersChatDto(userId);
    }

    @GetMapping(value = "/chat/{chatId}")
    public List<MessageDto> getChatMessages(@PathVariable Long chatId) {
        return chatService.getMessagesDto(chatId);
    }

    @GetMapping(value = "/chat/start/{receiverId}")
    public ChatDto startChatting(Authentication authentication, @PathVariable Long receiverId) {
        Long senderId = ((User)authentication.getPrincipal()).getId();
        return chatService.getChatAndCreateIfNotExists(senderId, receiverId);
    }


}
