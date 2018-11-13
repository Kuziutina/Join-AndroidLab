package ru.kpfu.itis.androidlab.Join.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultForm {
    private Integer code;
    private String error;
    private ResponseForm responseForm;
}
