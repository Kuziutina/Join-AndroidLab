package ru.kpfu.itis.androidlab.Join.util;

import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class EmailSender {

    private JavaMailSender javaMailSender;
    private ExecutorService executorService;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        executorService = Executors.newCachedThreadPool();
    }

//    @SneakyThrows
    public void sendEmailMessage(String destEmail, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setContent(text, "text/html");
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(destEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            executorService.submit(() -> javaMailSender.send(mimeMessage));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}