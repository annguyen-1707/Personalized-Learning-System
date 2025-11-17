package vn.fu_ohayo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.repository.AdminRepository;
import vn.fu_ohayo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminServiceDetail {
    private final AdminRepository userRepository;

//    public AdminServiceDetail adminServiceDetail()  {
//        return username -> userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//    }
}
