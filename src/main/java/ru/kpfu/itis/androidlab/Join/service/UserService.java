package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.androidlab.Join.dto.*;
import ru.kpfu.itis.androidlab.Join.form.*;
import ru.kpfu.itis.androidlab.Join.helper.ImageHelper;
import ru.kpfu.itis.androidlab.Join.model.*;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceInt {

    private ConfirmationServiceInt confirmationService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SpecializationServiceInt specializationService;
    private ProjectServiceInt projectService;
    private NotificationServiceInt notificationService;
    private ImageHelper imageHelper;

    public UserService(UserRepository userRepository,
                       ConfirmationServiceInt confirmationService,
                       PasswordEncoder passwordEncoder,
                       SpecializationServiceInt specializationService,
                       @Lazy ProjectServiceInt projectService,
                       @Lazy NotificationServiceInt notificationService,
                       ImageHelper imageHelper) {
        this.userRepository = userRepository;
        this.confirmationService = confirmationService;
        this.passwordEncoder = passwordEncoder;
        this.specializationService = specializationService;
        this.projectService = projectService;
        this.notificationService = notificationService;
        this.imageHelper = imageHelper;
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
    public ResultForm updateWithSpec(Long id, ProfileForm profileForm) {
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
        specializationService.deleteSpecialization(user);

        for (SpecializationForm specializationForm: profileForm.getSpecializations()) {
            ResultForm resultForm = specializationService.addSpecialization(user, specializationForm);
            if (resultForm.getCode() != 200) return resultForm;
        }

        userRepository.save(user);
        return ResultForm.builder().code(200).build();
    }

    @Override
    public void changeUserPassword(ChangePasswordForm changePasswordForm) {
        User user = getUserByEmail(changePasswordForm.getEmail());
        user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void addProfileImage(String url, Long id) {
        User user = getUser(id);
        user.setProfileImageLink(url);

        userRepository.save(user);
    }

    @Override
    public List<NotificationDto> getUserNotifications(Long id) {
        User user = getUser(id);
        return notificationService.getUserNotificationDtos(user);
    }

    @Override
    public List<SimpleUserDto> findUserDtos(String username, String vacancyName, Integer level, Integer experience, Long projectId) {
        List<User> users = findUsers(username, vacancyName, level, experience);
        users.remove(projectService.getLeader(projectId));
        List<User> justThere = projectService.getAllParticipants(projectId);
        List<User> justInvited = notificationService.getInvitedUser(projectId);
        List<User> justJoined = notificationService.getJoinedUser(projectId);
        List<SimpleUserDto> userDtos = new ArrayList<>();

        SimpleUserDto simpleUserDto;
        for (User user: users) {
            simpleUserDto = SimpleUserDto.from(user);
            if (justThere.contains(user)) {
                simpleUserDto.setStatus(1);
            }
            else if (justInvited.contains(user)) {
                simpleUserDto.setStatus(2);
            }
            else if (justJoined.contains(user)) {
                simpleUserDto.setStatus(3);
            }
            userDtos.add(simpleUserDto);
        }
        return userDtos;
    }

    @Override
    public String imageUpload(MultipartFile multipartFile, Long userId) {
        String url = imageHelper.uploadImage(multipartFile);
        if (url != null) {
            User user = getUser(userId);
            if (user.getProfileImageLink() != null) imageHelper.deleteImage(user.getProfileImageLink());
            user.setProfileImageLink(url);
            userRepository.save(user);
        }

        return url;
    }

    @Override
    public boolean deleteImage(Long userId) {
        User user = getUser(userId);
        if (user.getProfileImageLink() != null && imageHelper.deleteImage(user.getProfileImageLink())) {
            user.setProfileImageLink(null);
            return true;
        }

        return false;
    }

    private List<User> findUsers(String username, String vacancyName, Integer level, Integer experience) {
        List<User> users;
        List<User> resultUser = new ArrayList<>();
        if (username != null) {
            users = userRepository.searchUsersByUsername("%"+username+"%");
        }
        else {
            users = userRepository.findAll();
        }

        if(vacancyName != null) {
            SpecializationName specializationName = specializationService.findSpecializationName(vacancyName);
            for (User user: users) {
                for (Specialization specialization: user.getSpecializations()) {
                    if (specialization.getSpecializationName().equals(specializationName)) {
                        if (level != null) {
                            if (level == specialization.getKnowledgeLevel()) {
                                if (experience != null) {
                                    if (experience == specialization.getExperience()) {
                                        resultUser.add(user);
                                    }
                                    else break;
                                }
                                else resultUser.add(user);
                            }
                            else break;
                        }
                        else if (experience != null) {
                            if (experience == specialization.getExperience()) {
                                resultUser.add(user);
                            }
                        }
                        else {
                            resultUser.add(user);
                        }
                        break;
                    }
                }
            }
        }
        else resultUser = users;

        return resultUser;
    }


}
