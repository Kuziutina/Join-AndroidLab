package ru.kpfu.itis.androidlab.Join.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.androidlab.Join.dto.*;
import ru.kpfu.itis.androidlab.Join.form.*;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.SpecializationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController extends MainController{
    private UserServiceInt userService;
    private SpecializationServiceInt specializationService;
    private ProjectServiceInt projectService;

    public UserController(UserServiceInt userService,
                          SpecializationServiceInt specializationService,
                          ProjectServiceInt projectService) {
        this.userService = userService;
        this.specializationService = specializationService;
        this.projectService = projectService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable Long id) {

        UserDto userDto = userService.getUserProfile(id);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping(value = "/{id}/add_specialization")
    public ResponseEntity addSpecialization(@PathVariable Long id, @RequestBody SpecializationForm specializationForm) {
        User user = userService.getUser(id);
        ResultForm responseForm = specializationService.addSpecialization(user, specializationForm);

        return createResponseEntity(responseForm);
    }

    @PostMapping(value = "/{id}/change")
    public ResponseEntity<ResponseForm> changeProfile(@PathVariable Long id, @RequestBody ProfileForm profileForm) {
        ResultForm resultForm = userService.change(id, profileForm);
        return createResponseEntity(resultForm);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResponseForm> updateProfileWithSpec(@PathVariable Long id, @RequestBody ProfileForm profileForm) {
        ResultForm resultForm = userService.updateWithSpec(id, profileForm);
        return createResponseEntity(resultForm);
    }

    @GetMapping(value = "/{id}/upload")
    @ResponseBody
    public String testUp(@PathVariable Long id) {
        return "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<body>\n" +
                "\n" +
                "<h1>Spring Boot file upload example</h1>\n" +
                "\n" +
                "<form method=\"POST\" action=\"/user/"+ id +"/upload\" enctype=\"multipart/form-data\">\n" +
                "    <input type=\"file\" name=\"file\" /><br/><br/>\n" +
                "    <input type=\"submit\" value=\"Submit\" />\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/{id}/upload")
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        String url = userService.imageUpload(file, id);
        if (url == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error", "image did not load");
            return ResponseEntity.status(400).headers(headers).build();
        }

        return ResponseEntity.ok(new ImageResponse(url));
    }

    @DeleteMapping(value = "/{id}/profileImage")
    public ResponseEntity deleteProfileImage(@PathVariable Long id) {
        if (userService.deleteImage(id)) {
            return ResponseEntity.ok().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("error", "inafe doesn't exist");
        return ResponseEntity.status(400).headers(headers).build();
    }


    @GetMapping(value = "/{id}/projects")
    public ResponseEntity userProject(@PathVariable Long id) {
        List<ProjectDto> projects = projectService.getUserProjectDtos(id);

        return ResponseEntity.ok(projects);
    }

    @GetMapping(value = "/{id}/notifications")
    public ResponseEntity userNotification(@PathVariable Long id) {
        List<NotificationDto> notificationDtos = userService.getUserNotifications(id);

        return ResponseEntity.ok(notificationDtos);
    }

    @GetMapping(value = "/search")
    public ResponseEntity searchUser(@RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "specialization_name", required = false) String vacancyName,
                                     @RequestParam(value = "knowledge_level", required = false) Integer level,
                                     @RequestParam(value = "experience", required = false) Integer experience,
                                     @RequestParam(value = "project_id", required = false) Long projectId) {
        List<SimpleUserDto> userDtos = userService.findUserDtos(username, vacancyName, level, experience, projectId);

        return ResponseEntity.ok(userDtos);
    }

    @PostMapping(value = "/invite")
    public ResponseEntity inviteUser(@RequestBody InviteUserForm inviteUserForm) {

        projectService.inviteUser(inviteUserForm);

        return ResponseEntity.ok().build();
    }


}
