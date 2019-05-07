package ru.kpfu.itis.androidlab.Join.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.Message;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long chatId;
    private Long receiverId;
    private Long senderId;
    private String text;
    private Date date;

    @JsonCreator
    public MessageDto(Long chatId, Long receiverId, Long senderId, String text, Date date) {
        super();
        this.chatId = chatId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.text = text;
        this.date = date;
    }



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
