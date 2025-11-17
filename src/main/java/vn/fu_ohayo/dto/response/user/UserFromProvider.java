package vn.fu_ohayo.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserFromProvider {
    String email;
    boolean isExist;
}
