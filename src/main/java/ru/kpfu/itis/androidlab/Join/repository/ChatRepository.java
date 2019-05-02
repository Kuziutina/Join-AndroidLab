package ru.kpfu.itis.androidlab.Join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.androidlab.Join.model.Chat;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByReceiverOrSender(User sender, User receiver);

    Chat findBySenderAndReceiver(User sender, User receiver);

}
