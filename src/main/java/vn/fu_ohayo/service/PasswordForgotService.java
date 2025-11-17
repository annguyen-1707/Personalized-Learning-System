package vn.fu_ohayo.service;

public interface PasswordForgotService {
    void createAndSendToken(String email);
//    void userInputPassword();
    boolean resetPassword(String token, String newPassword);
//    String hashPassword(String plainPassword);
    String generateToken(String email);
}