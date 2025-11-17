package vn.fu_ohayo.constant;

public class ConstantGolbal {
    public static final String HTTP_SUCCESS_RESPONSE = "success";
    public static final String HTTP_SUCCESS_CODE_RESPONSE = "200";

    public static final String CREATE_SUCCESS_CODE = "201"; // Created
    public static final String READ_SUCCESS_CODE = "200";   // OK
    public static final String UPDATE_SUCCESS_CODE = "200"; // OK
    public static final String DELETE_SUCCESS_CODE = "204"; // No Content
    public static final String RECOVER_SUCCESS_CODE = "204"; // No Content

    // Success Messages
    public static final String CREATE_SUCCESS_MESSAGE = "Create successfully";
    public static final String READ_SUCCESS_MESSAGE = "Fetch successfully";
    public static final String UPDATE_SUCCESS_MESSAGE = "Update successfully";
    public static final String DELETE_SUCCESS_MESSAGE = "Delete successfully";
    public static final String RECOVER_SUCCESS_MESSAGE = "Recover successfully";

    private ConstantGolbal() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
