package ru.kpfu.itis.androidlab.Join.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.androidlab.Join.dto.NotificationDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleUserDto;
import ru.kpfu.itis.androidlab.Join.dto.UserDto;
import ru.kpfu.itis.androidlab.Join.form.*;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface UserServiceInt {
    ResponseForm registerUser(RegistrationForm registrationForm);
    UserDto getUserProfile(Long id);
    User getUser(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    ResultForm change(Long id, ProfileForm profileForm);
    ResultForm updateWithSpec(Long id, ProfileForm profileForm);
    void changeUserPassword(ChangePasswordForm changePasswordForm);
    void addProfileImage(String url, Long id);

    List<NotificationDto> getUserNotifications(Long id);

    List<SimpleUserDto> findUserDtos(String username, String vacancyName, Integer level, Integer experience, Long projectId);

    String imageUpload(MultipartFile multipartFile, Long userId);

    boolean deleteImage(Long userId);
}
