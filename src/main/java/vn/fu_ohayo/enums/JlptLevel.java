package vn.fu_ohayo.enums;

public enum JlptLevel {

    N1, N2, N3, N4, N5;

    public static JlptLevel fromString(String level) {
        for (JlptLevel jlptLevel : JlptLevel.values()) {
            if (jlptLevel.name().equalsIgnoreCase(level)) {
                return jlptLevel;
            }
        }
        throw new IllegalArgumentException("No enum constant " + JlptLevel.class.getCanonicalName() + "." + level);
    }
}
