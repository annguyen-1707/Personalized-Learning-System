package vn.fu_ohayo.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.config.VnPayProperties;
import vn.fu_ohayo.entity.MembershipLevelOfUser;
import vn.fu_ohayo.entity.Notification;
import vn.fu_ohayo.entity.Payment;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.PaymentStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class VnpayService {
    VnPayProperties vnPayProperties;
    UserRepository userRepository;
    PaymentRepository paymentRepository;
    MemberShipLevelRepository memberShipLevelRepository;
    MemberShipLevelOfUserRepository memberShipLevelOfUserRepository;
    NotificationRepository notificationRepository;
    NotificationService notificationService;

    public String createPaymentUrl(HttpServletRequest request, long amount, Long orderInfo) {
        // Build các tham số
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        Notification notification = notificationRepository.findById(orderInfo).orElseThrow();
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnPayProperties.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount * 100)); // nhân 100 theo yêu cầu VNPAY
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", id);
        params.put("vnp_OrderInfo", Long.toString(orderInfo));
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnPayProperties.getReturnUrl());
        params.put("vnp_IpAddr", request.getRemoteAddr());
        params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        User user = userRepository.findById(notification.getUserSend().getUserId())
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Payment payment = Payment.builder()
                .amount(amount)
                .orderId(id)
                .user(user)
                .status(vn.fu_ohayo.enums.PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);
        // Sort các tham số
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String name : fieldNames) {
            String value = params.get(name);
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            query.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
        }

        // Tạo chữ ký
        String secureHash = HmacSHA512(vnPayProperties.getHashSecret(), hashData.toString());
        query.append("vnp_SecureHash=").append(secureHash);

        return vnPayProperties.getPayUrl() + "?" + query.toString();
    }

    private String HmacSHA512(String key, String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo HMAC", e);
        }
    }

    public String processVNPayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (!entry.getKey().equals("vnp_SecureHash") && !entry.getKey().equals("vnp_SecureHashType")) {
                fields.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String responseCode = request.getParameter("vnp_ResponseCode");

        String hashData = hashAllFields(fields);
        String myCheckSum = HmacSHA512(vnPayProperties.getHashSecret(), hashData);
        String orderId = request.getParameter("vnp_TxnRef");
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new AppException(ErrorEnum.ORDER_ID_NOT_FOUND));

        if (myCheckSum.equals(vnp_SecureHash)) {
            if ("00".equals(responseCode)) {
                Long notificationId = Long.parseLong(request.getParameter("vnp_OrderInfo"));
                Long userId = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException()).getUserSend().getUserId();
                long amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;
                notificationService.handleNotificationAction(notificationId, true);

                int endDays = (int) (amount / 1000);
                MembershipLevelOfUser membershipLevelOfUser;
                if(memberShipLevelOfUserRepository.existsByUserUserId(userId)) {
                    membershipLevelOfUser = memberShipLevelOfUserRepository.findByUserUserId(userId);
                    LocalDate oldEndDate = membershipLevelOfUser.getEndDate();
                    long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), oldEndDate);
                    if (daysRemaining > 0) {
                        endDays += (int) daysRemaining;
                    }
                    membershipLevelOfUser.setEndDate(LocalDate.now().plusDays(endDays));
                    membershipLevelOfUser.setStartDate(LocalDate.now());
                    log.info("AAA" +memberShipLevelRepository.findByPrice(amount));
                    membershipLevelOfUser.setMembershipLevel(memberShipLevelRepository.findByPrice(amount));
                    User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
                    user.setMembershipLevel(memberShipLevelRepository.findByPrice(amount).getName());
                    userRepository.save(user);
                }
                else {
                    membershipLevelOfUser = MembershipLevelOfUser.builder()
                            .membershipLevel(memberShipLevelRepository.findByPrice(amount))
                            .user(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)))
                            .endDate(LocalDate.now().plusDays(endDays))
                            .membershipLevel(memberShipLevelRepository.findByPrice(amount))
                            .build();
                }
               memberShipLevelOfUserRepository.save(membershipLevelOfUser);

                 payment.setStatus(PaymentStatus.SUCCESS);
                 payment.setBankCode(request.getParameter("vnp_BankCode"));
                paymentRepository.save(payment);

                return "Thanh toán thành công cho đơn hàng " + orderId + ", số tiền: " + amount + " VND";
            } else if("02".equals(responseCode)) {
                return "Thanh toán thất bại. Mã lỗi: " + responseCode;
            }
            else {
                String message = switch (responseCode) {
                    case "24" -> "Bạn đã hủy giao dịch.";
                    case "51" -> "Tài khoản không đủ tiền.";
                    case "09" -> "Thẻ chưa đăng ký Internet Banking.";
                    case "05" -> "Chữ ký không hợp lệ.";
                    default -> "Giao dịch không thành công. Mã lỗi: " + responseCode;
                };
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                return message;
            }
        } else {
            return "Sai chữ ký! Dữ liệu không hợp lệ.";
        }
    }


    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (String name : fieldNames) {
            String value = fields.get(name);
            if (value != null && value.length() > 0) {
                sb.append(name).append('=').append(value);
                sb.append('&');
            }
        }
        // Xoá dấu & cuối cùng
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }


}