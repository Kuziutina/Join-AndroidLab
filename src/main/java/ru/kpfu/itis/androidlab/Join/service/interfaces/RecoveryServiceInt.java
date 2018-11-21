package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.form.ChangePasswordForm;
import ru.kpfu.itis.androidlab.Join.model.Recovery;

public interface RecoveryServiceInt {
    void sendRecoveryLetter(String email);
    void changePassword(ChangePasswordForm changePasswordForm);
    Recovery getRecovery(ChangePasswordForm changePasswordForm);
}
