package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {


    //Desc or asc?????????

    Message findFirstByChatOrderByDate(Chat chat);

    List<Message> findAllByChatOrderByDate(Chat chat);

}
