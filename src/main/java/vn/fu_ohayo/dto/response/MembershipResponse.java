package vn.fu_ohayo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.MembershipLevel;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipResponse {
    MembershipLevel name;
    Integer durationInDays;
    private Long price;
}
