package vn.fu_ohayo.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.impl.AuthenticationServiceImp;


@Controller
@RequestMapping("/mail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class EmailController {

    AuthenticationServiceImp authenticationService;
    MailService mailService;
    @GetMapping("/mail-confirm")
    public String introspectToken(@RequestParam("token") String token) {
        boolean result = authenticationService.extractToken(token, TokenType.ACCESS_TOKEN);

        if (result) {
            return "success";
        } else {
            return "error";
        }
    }
}




