package vn.fu_ohayo.enums;

public enum ContentTypeEnum {
    Listening,
    Reading,
    Speaking;

    public static ContentTypeEnum fromString(String contentType) {
        for (ContentTypeEnum t : ContentTypeEnum.values()) {
            if (t.name().equalsIgnoreCase(contentType)) {
                return t;
            }
        }
        throw new IllegalArgumentException("No enum constant " + ContentTypeEnum.class.getCanonicalName() + "." + contentType);
    }
}