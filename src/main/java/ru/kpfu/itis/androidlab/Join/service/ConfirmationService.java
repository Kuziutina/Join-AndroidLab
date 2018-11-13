package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.androidlab.Join.model.Confirmation;
import ru.kpfu.itis.androidlab.Join.repository.ConfirmationRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ConfirmationServiceInt;
import ru.kpfu.itis.androidlab.Join.util.EmailSender;
import ru.kpfu.itis.androidlab.Join.util.Generator;

@Service
@Transactional
public class ConfirmationService implements ConfirmationServiceInt {

    private EmailSender emailSender;
    private ConfirmationRepository confirmationRepository;

    public ConfirmationService(EmailSender emailSender,
                               ConfirmationRepository confirmationRepository) {
        this.emailSender = emailSender;
        this.confirmationRepository = confirmationRepository;
    }

    public void sendConfirmationLetter(String email) {
        String codeConfirmed = Generator.generate(6);
        Confirmation confirmation = confirmationRepository.findByEmail(email);
        if (confirmation == null) {
            confirmation = Confirmation.builder().email(email).build();
        }
        confirmation.setCode(codeConfirmed);
        confirmationRepository.save(confirmation);
        emailSender.sendEmailMessage(email, "Подтверждение почты", "Код для подтверждения " + codeConfirmed);
    }

    public void deleteConfirmation(String email) {
        confirmationRepository.deleteByEmail(email);
    }

}
