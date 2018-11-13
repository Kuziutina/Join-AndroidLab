package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.dto.UserDto;
import ru.kpfu.itis.androidlab.Join.form.ProfileForm;
import ru.kpfu.itis.androidlab.Join.form.ResponseForm;
import ru.kpfu.itis.androidlab.Join.form.ResultForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.SpecializationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController extends MainController{
    private UserServiceInt userService;
    private SpecializationServiceInt specializationService;

    public UserController(UserServiceInt userService,
                          SpecializationServiceInt specializationService) {
        this.userService = userService;
        this.specializationService = specializationService;
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

}
