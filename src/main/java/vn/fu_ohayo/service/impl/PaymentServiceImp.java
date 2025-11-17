package vn.fu_ohayo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import vn.fu_ohayo.dto.request.PaymentRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.PaymentInfoResponse;
import vn.fu_ohayo.entity.MembershipLevelOfUser;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.entity.ParentStudent;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.NotificationEnum;
import vn.fu_ohayo.enums.ParentCodeStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.*;
import vn.fu_ohayo.service.NotificationService;
import vn.fu_ohayo.service.PaymentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "PAYMENT_SERVICE")
public class PaymentServiceImp implements PaymentService {
    MemberShipLevelOfUserRepository memberShipLevelOfUserRepository;
    UserRepository userRepository ;
    ParentStudentRepository parentStudentRepository;
    PaymentRepository paymentRepository;
    NotificationRepository notificationRepository;
    @Override
    public ApiResponse<String> paymentInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        MembershipLevelOfUser membershipLevelOfUser = memberShipLevelOfUserRepository.findByUserUserId(user.getUserId());
        String mess = "";
        if(membershipLevelOfUser != null) {
            mess += "Your monthly subscription is valid until " + membershipLevelOfUser.getEndDate();
        }
        return ApiResponse.<String>builder().status("OK").message("Sucess").data(mess).code("200").build();
    }

    @Override
    public boolean createOrder() {
        return false;
    }

    @Override
    public void sendRequestToParent(PaymentRequest paymentRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        List<ParentStudent> list = parentStudentRepository.findByStudentEmail(email).stream().filter(parentStudent ->parentStudent.getParentCodeStatus()!= null && parentStudent.getParentCodeStatus().equals(ParentCodeStatus.CONFIRM)).toList();
        Long count = notificationRepository.countTodayPaymentRequests(user.getUserId());
        log.info(String.valueOf(count));
        if(count >= 3) {
            throw new AppException(ErrorEnum.REQUEST_PAYMENT);
        }
        String content = "Student: " + user.getFullName() + "want to buy the VIP package: " + paymentRequest.getAmount() + " VND";

        list.forEach(parentStudent ->{
                    Notification notification = Notification.builder()
                            .type(NotificationEnum.PAYMENT)
                            .title(NotificationEnum.PAYMENT.getTitle())
                            .content(content)
                            .statusSend(false)
                            .user(parentStudent.getParent())
                            .userSend(user)
                            .build();
                    notificationRepository.save(notification);
            log.info(content);
                });



    }

}
