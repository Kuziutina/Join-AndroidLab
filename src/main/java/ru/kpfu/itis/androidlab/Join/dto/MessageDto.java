package ru.kpfu.itis.androidlab.Join.dto;

import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.Message;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Long chatId;
    private Long receiverId;
    private Long senderId;
    private String text;
    private Date date;


    public static MessageDto from (Message message) {
        if (message == null) return null;
        MessageDto messageDto = MessageDto.builder()
                                    .chatId(message.getChat().getId())
                                    .date(message.getDate())
                                    .receiverId(message.getReceiver().getId())
                                    .senderId(message.getSender().getId())
                                    .text(message.getText())
                                    .build();

        return messageDto;
    }
}
