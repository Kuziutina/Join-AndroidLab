package ru.kpfu.itis.androidlab.Join.service;

import com.google.common.hash.Hashing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.*;
import ru.kpfu.itis.androidlab.Join.form.*;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ConfirmationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;
import ru.kpfu.itis.androidlab.Join.util.EmailSender;
import ru.kpfu.itis.androidlab.Join.util.Generator;

import java.nio.charset.StandardCharsets;

@Service
public class UserService implements UserServiceInt {

    private ConfirmationServiceInt confirmationService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       ConfirmationServiceInt confirmationService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.confirmationService = confirmationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseForm registerUser(RegistrationForm registrationDto) {
        User user = User.builder().username(registrationDto.getUsername())
                                .email(registrationDto.getEmail())
                                .password(passwordEncoder.encode(registrationDto.getPassword()))
                                .build();
        userRepository.save(user);
        confirmationService.deleteConfirmation(registrationDto.getEmail());

        return new AuthResponseDto();
    }

    @Override
    public UserDto getUserProfile(Long id) {
        User user = userRepository.getOne(id);

        return UserDto.from(user);
    }

    @Override
    public User getUser(Long id) {
        User user = userRepository.getOne(id);

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public ResultForm change(Long id, ProfileForm profileForm) {
        User user = getUser(id);
        User checkUser = userRepository.findUserByUsername(profileForm.getUsername());
        if (checkUser != null && !user.equals(checkUser)) {
            return ResultForm.builder().code(400).error("Invalid username").build();
        }
        checkUser = userRepository.findUserByEmail(profileForm.getEmail());
        if (checkUser != null && !user.equals(checkUser)) {
            return ResultForm.builder().code(400).error("Invalid email").build();
        }
        user.setName(profileForm.getName());
        user.setLastName(profileForm.getLastname());
        user.setUsername(profileForm.getUsername());
        user.setEmail(profileForm.getEmail());
        user.setPhone(profileForm.getPhoneNumber());

        userRepository.save(user);
        return ResultForm.builder().code(200).build();
    }

    @Override
    public void changeUserPassword(ChangePasswordForm changePasswordForm) {
        User user = getUserByEmail(changePasswordForm.getEmail());
        user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
        userRepository.save(user);
    }


}
