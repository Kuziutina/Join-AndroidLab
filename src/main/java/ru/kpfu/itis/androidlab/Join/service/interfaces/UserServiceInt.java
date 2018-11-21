package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.dto.UserDto;
import ru.kpfu.itis.androidlab.Join.form.*;
import ru.kpfu.itis.androidlab.Join.model.User;

public interface UserServiceInt {
    ResponseForm registerUser(RegistrationForm registrationForm);
    UserDto getUserProfile(Long id);
    User getUser(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    ResultForm change(Long id, ProfileForm profileForm);
    void changeUserPassword(ChangePasswordForm changePasswordForm);
}
