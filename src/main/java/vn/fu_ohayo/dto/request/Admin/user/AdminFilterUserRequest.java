package vn.fu_ohayo.dto.request.admin.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import vn.fu_ohayo.dto.request.PagingRequest;
import vn.fu_ohayo.enums.MembershipLevel;
import vn.fu_ohayo.enums.UserStatus;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminFilterUserRequest extends PagingRequest {

    String fullName;

    MembershipLevel membershipLevel;

    UserStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date registeredTo;

    Boolean isDeleted;
}
