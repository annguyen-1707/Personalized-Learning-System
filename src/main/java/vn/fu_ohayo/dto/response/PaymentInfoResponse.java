package vn.fu_ohayo.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.fu_ohayo.enums.MembershipLevel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PaymentInfoResponse {
    LocalDate endDate;
    MembershipLevel membershipLevel;
}
