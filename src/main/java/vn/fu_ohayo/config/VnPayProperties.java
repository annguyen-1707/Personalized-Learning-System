package vn.fu_ohayo.config;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vnpay")
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VnPayProperties {
     String tmnCode;
     String hashSecret;
     String payUrl;
     String returnUrl;
}
