package vn.fu_ohayo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fu_ohayo.enums.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/options")
public class OptionController {
    @GetMapping("/user-statuses")
    public List<UserStatus> getUserStatuses() {
        return Arrays.asList(UserStatus.values());
    }

    @GetMapping("/membership-levels")
    public List<MembershipLevel> getMembershipLevels() {
        return Arrays.asList(MembershipLevel.values());
    }

    @GetMapping("/jlpt-levels")
    public List<JlptLevel> getJlptLevels() {
        return Arrays.asList(JlptLevel.values());
    }

    @GetMapping("/part-of-speech")
    public List<PartOfSpeech> getPartOfSpeech() {
        return Arrays.asList(PartOfSpeech.values());
    }

    @GetMapping("/roles")
    public List<RoleEnum> getRoles() {
        return Arrays.asList(RoleEnum.values());
    }
}
