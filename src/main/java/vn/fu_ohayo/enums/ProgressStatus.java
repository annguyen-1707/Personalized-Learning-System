package vn.fu_ohayo.enums;

public enum ProgressStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED;

    public static ProgressStatus fromString(String status) {
        for (ProgressStatus progressStatus : ProgressStatus.values()) {
            if (progressStatus.name().equalsIgnoreCase(status)) {
                return progressStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant " + ProgressStatus.class.getCanonicalName() + "." + status);
    }
}
