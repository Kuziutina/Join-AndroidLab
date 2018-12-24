package ru.kpfu.itis.androidlab.Join.form;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SpecializationForm {
    private String name;
    private Integer knowledgeLevel;
    private Integer experience;
    private String technologies;

}
