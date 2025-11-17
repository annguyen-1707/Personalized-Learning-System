
package vn.fu_ohayo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.entity.UserProfileUpdateDTO;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.entity.User;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserUpdateController {

    private final UserRepository userRepository;

    @PostMapping("/edit")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        user.setFullName(request.getName());
        user.setPhone(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setDob(request.getDateOfBirth());
        user.setGender(request.getGender());
        userRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully");
    }
}