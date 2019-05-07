package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.MessageDto;
import ru.kpfu.itis.androidlab.Join.helper.NotificationHelper;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.model.Message;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.repository.MessageRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ChatServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.MessageServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.util.LinkedList;
import java.util.List;

@Service
public class MessageService implements MessageServiceInt{

    private MessageRepository messageRepository;
    private UserServiceInt userService;
    private ChatServiceInt chatService;
    private NotificationHelper notificationHelper;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserServiceInt userService,
                          @Lazy ChatServiceInt chatService,
                          NotificationHelper notificationHelper) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
        this.notificationHelper = notificationHelper;
    }

    @Override
    public void addMessage(MessageDto messageDto) {
        Message message = Message.builder().receiver(userService.getUser(messageDto.getReceiverId()))
                            .sender(userService.getUser(messageDto.getSenderId()))
                            .date(messageDto.getDate())
                            .text(messageDto.getText())
                            .chat(chatService.getChat(messageDto.getChatId()))
                            .build();

        messageRepository.save(message);

        //TODO fix lazy exception
//        if (message.getReceiver() != null && message.getReceiver().getOnline()) {
//            notificationHelper.sendMessageNotification(message);
//        }

    }



    @Override
    public Message getLastMessage(Chat chat) {

        //TODO exception (if chat not exist or chat haven't message)
        return messageRepository.findFirstByChatOrderByDate(chat);
    }

    @Override
    public List<MessageDto> getMessageDtos(Chat chat) {
        List<Message> messages = getMessage(chat);

        return messageDtosFrom(messages);
    }

    private List<Message> getMessage(Chat chat) {
        return messageRepository.findAllByChatOrderByDate(chat);
    }

    private List<MessageDto> messageDtosFrom(List<Message> messages) {
        List<MessageDto> messageDtos = new LinkedList<>();

        for (Message message: messages) {
            messageDtos.add(MessageDto.from(message));
        }

        return messageDtos;
    }
}
