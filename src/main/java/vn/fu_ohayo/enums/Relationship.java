package vn.fu_ohayo.enums;

public enum Relationship {
    FATHER,MOTHER,SON,DAUGHTER,SIBLING,GRANDPARENT,GRANDCHILD,UNCLE,AUNT,COUSIN,OTHER;
    public static Relationship fromString(String relationship) {
        for (Relationship r : Relationship.values()) {
            if (r.name().equalsIgnoreCase(relationship)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Relationship.class.getCanonicalName() + "." + relationship);
    }
}
