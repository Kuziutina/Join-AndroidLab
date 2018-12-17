package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.form.ResultForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.Specialization;
import ru.kpfu.itis.androidlab.Join.model.SpecializationName;
import ru.kpfu.itis.androidlab.Join.model.User;

public interface SpecializationServiceInt {
    ResultForm addSpecialization(User user, SpecializationForm specializationForm);
    ResultForm update(Long id, SpecializationForm specializationForm);
    ResultForm delete(Long id);

    Specialization addSpecialization(Project project, SpecializationForm specializationForm);
    void deleteSpecialization(Project project);
    void deleteSpecialization(User user);

    SpecializationName findSpecializationName(String specializationName);
}
