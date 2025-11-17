package vn.fu_ohayo.entity;
import lombok.Data;
import vn.fu_ohayo.enums.*;

@Data
public class UserProfileDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private UserStatus status;
    private MembershipLevel membershipLevel;
    private Provider provider;
    private String profilePicture;
    private boolean hasSettingsAccsees = true;
    private String memberSince;
}
