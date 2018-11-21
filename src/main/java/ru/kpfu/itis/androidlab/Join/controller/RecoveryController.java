package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kpfu.itis.androidlab.Join.form.ChangePasswordForm;
import ru.kpfu.itis.androidlab.Join.form.ConfirmationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.RecoveryServiceInt;

import javax.validation.Valid;

public class RecoveryController extends MainController{

    private RecoveryServiceInt recoveryService;

    public RecoveryController(RecoveryServiceInt recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping(value = "/recovery/email")
    public ResponseEntity emailConfirmation(@Valid @RequestBody ConfirmationForm confirmationForm,
                                            BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        recoveryService.sendRecoveryLetter(confirmationForm.getEmail());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/recovery")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm,
                                         BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        recoveryService.changePassword(changePasswordForm);
        return ResponseEntity.ok().body(null);
    }
}
