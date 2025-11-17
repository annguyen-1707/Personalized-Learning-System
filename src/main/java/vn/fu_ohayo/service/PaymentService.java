package vn.fu_ohayo.service;

import vn.fu_ohayo.dto.request.PaymentRequest;
import vn.fu_ohayo.dto.response.ApiResponse;

public interface PaymentService {
    ApiResponse<String> paymentInfo();
    boolean createOrder();
    void sendRequestToParent(PaymentRequest paymentRequest);
}
