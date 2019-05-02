package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.ChatDto;
import ru.kpfu.itis.androidlab.Join.dto.MessageDto;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.model.Message;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.ChatRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ChatServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.MessageServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.util.LinkedList;
import java.util.List;

@Service
public class ChatService implements ChatServiceInt{

    private MessageServiceInt messageService;
    private ChatRepository chatRepository;
    private UserServiceInt userService;


    @Autowired
    public ChatService(MessageServiceInt messageService,
                       ChatRepository chatRepository,
                       UserServiceInt userService) {
        this.messageService = messageService;
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public Message getLastMessage(Long chatId) {
        Chat chat = chatRepository.getOne(chatId);
        return messageService.getLastMessage(chat);
    }

    @Override
    public List<ChatDto> getUsersChatDto(Long userId) {
        User user = userService.getUser(userId);

        return createChatDtos(getUsersChat(user));
    }

    @Override
    public Chat getChat(Long chatId) {
        return chatRepository.getOne(chatId);
    }

    @Override
    public List<MessageDto> getMessagesDto(Long chatId) {
        Chat chat = getChat(chatId);
        return messageService.getMessageDtos(chat);
    }

    @Override
    public ChatDto getChatAndCreateIfNotExists(Long senderId, Long receiverId) {

        User sender = userService.getUser(senderId);
        User receiver = userService.getUser(receiverId);

        Chat chat = chatRepository.findBySenderAndReceiver(sender, receiver);
        if (chat == null) {
            chat = chatRepository.findBySenderAndReceiver(receiver, sender);
        }
        if (chat == null) {
            chat = Chat.builder()
                        .receiver(receiver)
                        .sender(sender)
                        .build();

            chatRepository.save(chat);
        }

        return ChatDto.from(chat);
    }

    private List<Chat> getUsersChat(User user) {
        return getUsersChatFromRepo(user);
    }

    private List<ChatDto> createChatDtos(List<Chat> chats) {
        List<ChatDto> chatDtos = new LinkedList<>();
        for (Chat chat: chats) {
            chatDtos.add(ChatDto.from(chat));
        }

        return chatDtos;
    }

    private List<Chat> getUsersChatFromRepo(User user) {
        return chatRepository.findAllByReceiverOrSender(user, user);
    }
}
