package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.dto.MessageDto;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.model.Message;

import java.util.List;

public interface MessageServiceInt {

    void addMessage(MessageDto messageDto);

    Message getLastMessage(Chat chat);

    List<MessageDto> getMessageDtos(Chat chat);
}
