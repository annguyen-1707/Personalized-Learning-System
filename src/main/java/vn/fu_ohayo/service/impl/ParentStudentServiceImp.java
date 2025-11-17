package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.NotificationEnum;
import vn.fu_ohayo.enums.ParentCodeStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.NotificationRepository;
import vn.fu_ohayo.repository.ParentStudentRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.NotificationService;
import vn.fu_ohayo.service.ParentStudentService;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "ParentStudent")
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParentStudentServiceImp implements ParentStudentService {
    private String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int CODE_LENGTH = 6;
    UserRepository userRepository;
    ParentStudentRepository parentStudentRepository;
    NotificationRepository notificationRepository;
    @Override
    public String generateCode() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Email lafa" ,email);
        parentStudentRepository.deleteAllExpiredUnlinkedCodes();
        long count = parentStudentRepository.countTodayCodesByParent(email);
        if(count == 5) {
            throw new AppException(ErrorEnum.EXCEED_DAILY_CODE_LIMIT);
        }
        List<ParentStudent> list = parentStudentRepository.findByParentEmail(email).stream().filter(parentStudent ->parentStudent.getParentCodeStatus() != null && parentStudent.getParentCodeStatus().equals(ParentCodeStatus.CONFIRM)).toList();
        if(list.size() == 3) {
            throw new AppException(ErrorEnum.MAX_STUDENT_LIMIT);
        }
        SecureRandom random = new SecureRandom();
        StringBuilder sb ;
        boolean isDuplicate = false;
        List<ParentStudent> parentStudents = parentStudentRepository.findAll();
        do {
            sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }

            isDuplicate = parentStudentRepository.existsByVerificationCode(sb.toString());

        } while (isDuplicate);

        User parent = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        parentStudentRepository.insertParentStudent(parent.getUserId(), sb.toString());
        return sb.toString();

}
    @Override
    public String addParentStudent() {
        return "";
    }

    @Override
    public String extractCode(String code) {
        ParentStudent parentStudent1 = parentStudentRepository.findByVerificationCode(code);
        log.info("CODE LA" + code);
        if(parentStudent1 == null) {
            return "The code isn't existed.";
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ParentStudent parentStudent = parentStudentRepository.findByVerificationCodeAndStudentEmail(code, email);
            if(parentStudent != null) {
                if( parentStudent.getParentCodeStatus().equals(ParentCodeStatus.PENDING) ){
                    return "Please check the notifications on your parent's page";
                }
                else if(parentStudent.getParentCodeStatus().equals(ParentCodeStatus.REJECT)) {
                    return "Code declined by your parent. Ask them to generate a new one";
                }
                else if(parentStudent.getParentCodeStatus().equals(ParentCodeStatus.CONFIRM)) {
                    return "Code verified. Please wait...";
                }
            }

            if(parentStudent1.getParentCodeStatus() != null) {
            return "The code already exists. Please try a different one.";
             }

        if (parentStudentRepository.findByStudentEmail(email).stream()
                .anyMatch(p -> p.getParentCodeStatus() == ParentCodeStatus.PENDING)) {
            return "You’ve just submitted a code. Please wait for the parent’s verification.";
        }
            User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
            parentStudent1.setStudent(user);
            parentStudent1.setParentCodeStatus(ParentCodeStatus.PENDING);
            String content = "Is Student: " + user.getFullName() + " has email: " + email + " your child";
        ParentStudent parent = parentStudentRepository.findByVerificationCode(code);

        Notification notification = Notification.builder()
                .type(NotificationEnum.ACCEPT_STUDENT)
                .title(NotificationEnum.ACCEPT_STUDENT.getTitle())
                .content(content)
                .statusSend(false)
                .user(parent.getParent())
                .userSend(user)
                .build();
        notificationRepository.save(notification);

        return "";
    }
}
