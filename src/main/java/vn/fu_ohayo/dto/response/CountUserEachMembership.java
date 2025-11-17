package vn.fu_ohayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.MembershipLevel;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CountUserEachMembership {
    MembershipLevel membershipLevel;
    int count;
}
