package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.form.ResponseForm;
import ru.kpfu.itis.androidlab.Join.form.ResultForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.SpecializationServiceInt;

import java.util.List;

@RestController
@RequestMapping(value = "/specialization")
public class SpecializationController extends MainController{

    private SpecializationServiceInt specializationService;

    public SpecializationController(SpecializationServiceInt specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping(value = "/{id}/update")
    public ResponseEntity updateSpecialization(@PathVariable Long id, @RequestBody SpecializationForm specializationForm) {
        ResultForm resultForm = specializationService.update(id ,specializationForm);
        return createResponseEntity(resultForm);
    }

    @PostMapping(value = "/{id}/delete")
    public ResponseEntity deleteSpecialization(@PathVariable Long id) {
        specializationService.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/name")
    public ResponseEntity getAllSpecializationName() {
        List<String> names = specializationService.getAllSpecializationName();

        return ResponseEntity.ok(names);
    }
}
