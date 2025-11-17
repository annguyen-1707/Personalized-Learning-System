package vn.fu_ohayo.enums;

public enum PermissionEnum {
    USER_READ,
    USER_WRITE,
    USER_DELETE,
    USER_UPDATE,
    CONTENT_READ,
    CONTENT_WRITE,
    CONTENT_DELETE,
    CONTENT_UPDATE;

    public static PermissionEnum fromString(String permission) {
        for (PermissionEnum p : PermissionEnum.values()) {
            if (p.name().equalsIgnoreCase(permission)) {
                return p;
            }
        }
        throw new IllegalArgumentException("No enum constant " + PermissionEnum.class.getCanonicalName() + "." + permission);
    }
}
