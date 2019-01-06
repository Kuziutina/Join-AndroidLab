package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.form.ChangePasswordForm;
import ru.kpfu.itis.androidlab.Join.model.Recovery;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.RecoveryRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.RecoveryServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;
import ru.kpfu.itis.androidlab.Join.util.EmailSender;
import ru.kpfu.itis.androidlab.Join.util.Generator;

@Service
public class RecoveryService implements RecoveryServiceInt {

    private RecoveryRepository recoveryRepository;
    private EmailSender emailSender;
    private UserServiceInt userService;

    public RecoveryService(RecoveryRepository recoveryRepository,
                           EmailSender emailSender,
                           UserServiceInt userService) {
        this.recoveryRepository = recoveryRepository;
        this.emailSender = emailSender;
        this.userService = userService;
    }


    @Override
    public void sendRecoveryLetter(String email) {
        String code = Generator.generate(6);
        User user = userService.getUserByEmail(email);
        Recovery recovery = getRecovery(email);
        if (recovery == null) {
            recovery = Recovery.builder().recoveryLink(code).user(user).build();
        }

        recovery.setRecoveryLink(code);

        recoveryRepository.save(recovery);
        emailSender.sendEmailMessage(email, "Восстановление пароля", "Код для восстановления пароля " + code);
    }

    @Override
    public void changePassword(ChangePasswordForm changePasswordForm) {

        userService.changeUserPassword(changePasswordForm);
        Recovery recovery = getRecovery(changePasswordForm);

        recoveryRepository.delete(recovery);
    }

    @Override
    public Recovery getRecovery(ChangePasswordForm changePasswordForm) {
        User user = userService.getUserByEmail(changePasswordForm.getEmail());
        return recoveryRepository.getByRecoveryLinkAndUser(changePasswordForm.getCode(), user);
    }


    private Recovery getRecovery(String email) {
        User user = userService.getUserByEmail(email);
        return recoveryRepository.getByUser(user);
    }
}
