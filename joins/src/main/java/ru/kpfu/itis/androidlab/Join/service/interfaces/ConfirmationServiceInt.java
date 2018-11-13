package ru.kpfu.itis.androidlab.Join.service.interfaces;

public interface ConfirmationServiceInt {
    void sendConfirmationLetter(String email);
    void deleteConfirmation(String email);
}
