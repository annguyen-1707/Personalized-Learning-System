package vn.fu_ohayo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.Provider;
@Data
@NoArgsConstructor

public class OAuthSocialRequest {
    String email;
    Provider provider;
}
