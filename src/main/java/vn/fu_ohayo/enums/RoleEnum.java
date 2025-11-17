package vn.fu_ohayo.enums;

public enum RoleEnum {
    USER,
    PARENT,
    CONTENT_MANAGER,
    USER_MANAGER,
    SUPER_ADMIN,
    STAFF;

    public static RoleEnum fromString(String role) {
        for (RoleEnum r : RoleEnum.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + RoleEnum.class.getCanonicalName() + "." + role);
    }
}
