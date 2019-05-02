package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.dto.ChatDto;
import ru.kpfu.itis.androidlab.Join.dto.MessageDto;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.model.Message;

import java.util.List;

public interface ChatServiceInt {
    Message getLastMessage(Long chatId);

    List<ChatDto> getUsersChatDto(Long userId);

    Chat getChat(Long chatId);

    List<MessageDto> getMessagesDto(Long chatId);

    ChatDto getChatAndCreateIfNotExists(Long senderId, Long receiverId);
}
