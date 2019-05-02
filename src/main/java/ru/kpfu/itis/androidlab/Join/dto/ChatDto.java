package ru.kpfu.itis.androidlab.Join.dto;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ChatServiceInt;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDto {

    private Long id;
    private UserDto sender;
    private UserDto receiver;
    private MessageDto lastMessage;

    public static ChatDto from(Chat chat) {
        if (chat == null) return null;
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chat.getId());
        chatDto.setSender(UserDto.from(chat.getSender()));
        chatDto.setReceiver(UserDto.from(chat.getReceiver()));
        if (chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            chatDto.setLastMessage(MessageDto.from(chat.getMessages().get(chat.getMessages().size() - 1)));
        }

        return chatDto;
    }

}
