package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.User;

public interface PasswordChangeService {
    boolean changePassword(User user, String currentPassword, String newPassword, String confirmPassword);
}