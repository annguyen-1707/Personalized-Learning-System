package vn.fu_ohayo.dto.request;

import lombok.Data;
import vn.fu_ohayo.enums.Provider;
@Data

public class OAuthRequest {
    String email;
    Provider provider;
}
