package ru.kpfu.itis.androidlab.Join.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddSpecializationResponseForm implements ResponseForm {
    long specializationId;
}
