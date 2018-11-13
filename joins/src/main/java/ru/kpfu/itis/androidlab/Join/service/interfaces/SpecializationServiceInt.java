package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.form.ResultForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.User;

public interface SpecializationServiceInt {
    ResultForm addSpecialization(User user, SpecializationForm specializationForm);
    ResultForm update(Long id, SpecializationForm specializationForm);
    ResultForm delete(Long id);
}
