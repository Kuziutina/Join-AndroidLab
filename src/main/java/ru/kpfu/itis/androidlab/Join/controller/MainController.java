package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.androidlab.Join.form.ResponseForm;
import ru.kpfu.itis.androidlab.Join.form.ResultForm;


public class MainController {

    ResponseEntity createValidErrorResponse(BindingResult errors) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        headers.add("error", errors.getAllErrors().get(0).getCode());
        return ResponseEntity.status(400).headers(headers).build();
    }

    public ResponseEntity<ResponseForm> createResponseEntity(ResultForm resultForm) {
        if (resultForm.getCode() == 200) {
            return ResponseEntity.ok(resultForm.getResponseForm());
        }
        else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("error", resultForm.getError());
            return ResponseEntity.status(resultForm.getCode()).headers(httpHeaders).body(resultForm.getResponseForm());
        }
    }
}
