package vn.fu_ohayo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {

    // 1xxx - validation
    INVALID_FIELDS("1001","Invalid fields"),
    INVALID_EMAIL("1002","Email is invalid"),
    INVALID_CATEGORY_CONTENT_READING("1003","Choose right category contentt" ),
    INVALID_ANSWER_CORRECT_COUNT("1004","Have to choose exactly one correct answer for the question." ),

    // 2xxx - authentication
    EMAIL_EXIST("2001","Email is used to register" ),
    PHONE_EXIST("2002", "Phone is used to register."),
    USERNAME_EXIST("2003","Username is used to register" ),
    EMAIL_OR_PASSWORD_INCORRECT("2004", "Email or password is incorrect."),
    INVALID_AGE("2005", "Your Age in range 5-100 years old."),
    // 3xxx - authorization
    ORDER_ID_NOT_FOUND("3001", "Order ID not found"),
    AUTH_FAILED ("3002", "Email or password is not exactly"),
    EXCEED_DAILY_CODE_LIMIT("3003", "You have already created 5 codes today. Please try again tomorrow."),
    MAX_STUDENT_LIMIT("3004", "You can manage up to 3 students only."),
    UNAUTHORIZED("401", "Access token expired"),
    FORBIDDEN("403", "Invalid token signature"),
    // 4xxx - not found
    ADMIN_NOT_FOUND("4000","Admin not found"),
    USER_NOT_FOUND("4001","User not found"),
    ROLE_NOT_FOUND("4002","Role not found"),
    INVALID_TOKEN("4003", "Invalid token"),
    REFRESH_TOKEN_NOT_FOUND ("4003", "Refresh token not found"),
    INTERNAL_SERVER_ERROR("5000", "Internal server error"),
    INVALID_CONTENT_SPEAKING("4003", "ContentSpeaking not found" ),
    FAVORITE_GRAMMAR_NOT_FOUND("4004", "Favorite grammar folder not found"),
    STUDY_REMINDER_NOT_FOUND("4005", "Study reminder not found"),
    SUBJECT_NOT_FOUND("4006", "Subject not found"),
    SUBJECT_CODE_EXISTS("4007", "Subject code is existed" ),
    EXERCISE_NOT_FOUND("4008", "Exercise not found"),
    QUESTION_NOT_FOUND("4009","Question not found" ),
    SUBJECT_NAME_EXISTS("4010",  "Subject name is existed"),
    SUBJECT_IN_USE("4011", "Subject is in use, cannot be deleted"),
    INVALID_PAGE("4012","Page is not exist" ),
    ACCOUNT_INACTIVE("4001","YOUR ACCOUNT IS IN ACTIVE"),
    LESSON_NOT_FOUND("4013",  "Lesson not found"),
    LESSON_NAME_EXIST( "4014", "Lesson is exited with this name." ),
    LESSON_HAS_VOCABULARY("4015", "Lesson has vocabularies, cannot be deleted"),
    LESSON_HAS_GRAMMAR("4016","Lesson has grammars, cannot be deleted" ),
    LESSON_IN_USE("4028", "Lesson is using with any user" ),
    SUBJECT_CONTENT_EMPTY("4030", "Can not accept with any content in the Subject not found." ),
    LESSON_NOT_COMPLETE("4031", "Can not accept with any content in the Lesson not found." ),


    VOCABULARY_EXISTS("4017", "Vocabulary already exists"),
    VOCABULARY_NOT_FOUND("4018", "Vocabulary not found with this kanji." ),
    GRAMMAR_EXISTED("4019", "Grammar is  existed in this lesson." ),
    GRAMMAR_NOT_FOUND("4020", "Grammar not found with this title." ),
    CONTENT_READING_NOT_FOUND("4021", "Content reading not found"),
    VOCABULARY_ALREADY_EXISTS_IN_CONTENT_READING("4022","Vocabulary is  existed in content reading " ),
    GRAMMAR_ALREADY_EXISTS_IN_CONTENT_READING("4023","Grammar is  existed in content reading " ),
    EXERCISE_QUESTION_NOT_FOUND("4024","No exercise questions found for this lesson exercise." ),
    USER_ALREADY_ENROLLED("4025", "User is already enrolled in subject." ),
    PROGRESS_NOT_FOUND("4026", "Progress not found for the user and subject." ),
    ANSWER_NOT_FOUND("4027","Can not found the answer." ),
    LIST_SUBJECT_NULL("4028", "List subject have at least 1 subject"),
    EXIST_AT_LEAST_CONTENT_LISTENING_OR_EXERCISE("4029", "Question exist at least in content listening or exercise"),
    PROGRESS_VOCABULARY_NOT_FOUND("4029", "Progress not found" ),
    DIALOGUE_NOT_FOUND ("4029","Dialogue not found"),
    INVALID_TYPE("4030","Type is not invalid (Exercise or content Listening)" ),
    CAN_NOT_ACCEPT("4031","Can not accept due to lack of internal content" ),
    // 5xxx - server error
    // 6xxx - PaymentErrorCode

    REQUEST_PAYMENT("6001","You send request for payment over 3 times / a day" ),
    AUTH_CODE_NOT_FOUND("6002", "Auth code not found." );




    //message validation
    public static final String INVALID_STATUS_MS = "Status is invalid";
    public static final String INVALID_MEMBERSHIP_MS = "Membership level is invalid";
    public static final String INVALID_EMAIL_MS = "Email is invalid";
    public static final String NOT_EMPTY_EMAIL = "Email must not be empty";
    public static final String NOT_EMPTY_PASSWORD = "Password must not be empty";
    public static final String INVALID_PASSWORD = "Password must be at least 5 characters";
    public static final String INVALID_NAME = "Fullname be more than 1 character and less than 50 charaters";
    public static final String INVALID_NAME2 = "Full name must have no numbers or special characters allowed";
    public static final String NOT_EMPTY_NAME = "Full name cannot be null";
    public static final String INVALID_PHONE = "Phone number must must start with 0 and  be between 10 and 12 digits";
    public static final String INVALID_ADDRESS = "Address must be less than 255 characters";
    public static final String INVALID_URL_AVATAR = "Avatar URL must be less than 255 characters";
    public static final String INVALID_URL_AUDIO = "File audio must be less than 255 characters";
    public static final String INVALID_ROLE = "Role must not be empty";
    public static final String NOT_EMPTY_USER = "User cannot be null";
    public static final String MAX_LENGTH_IMAGE = "Image URL must be less than 255 characters";
    public static final String NOT_EMPTY_CONTENT_TYPE = "Content type cannot be null";
    public static final String NOT_EMPTY_CATEGORY = "Category cannot be null";
    public static final String NOT_EMPTY_IMAGE = "Image cannot be null";
    public static final String NOT_EMPTY_URL = "File URL can not null";
    public static final String NOT_EMPTY_SUBJECT_CODE = "Subject code must not be empty";
    public static final String NOT_EMPTY_SUBJECT_NAME = "Subject name must not be empty";
    public static final String INVALID_SUBJECT_NAME = "Subject name must be less than 50 characters";
    public static final String NOT_EMPTY_JLPT_LEVEL = "JLPT level must not be empty";
    public static final String NOT_EMPTY_PART_OF_SPEECH = "Part of speech must not be empty";
    public static final String NOT_EMPTY_MEANING = "Meaning must not be null";
    public static final String NOT_EMPTY_ROMAJI = "Romaji must not be empty";
    public static final String NOT_EMPTY_SCRIPT = "script can not null";
    public static final String NOT_EMPTY_DATE = "Date can not null";
    public static final String NOT_EMPTY_TIME = "Time can not null";

    public static final String NOT_EMPTY_KANA = "Kana must not be empty";
    public static final String MAX_LENGTH_50 = "input be less than 50 characters";
    public static final String NOT_EMPTY_KANJI = "Kanji must not be empty";
    public static final String NOT_EMPTY_TITLE = "Title must not be empty";
    public static final String MAX_LENGTH_100 = "input must be less than 100 characters";
    public static final String NOT_EMPTY_STRUCTURE = "Structure must not be empty";
    public static final String MAX_LENGTH_200 = "input must be less than 200 characters";
    public static final String MAX_LENGTH_500 = "input must be less than 500 characters";
    public static final String MIN_TIME_1 = "Time must be greater than 1 minute";
    public static final String INVALID_THUMBNAIL_URL = "Thumbnail URL must be less than 255 characters";
    public static final String INVALID_VIDEO_URL = "Video URL must be less than 255 characters";
    public static final String NOT_EMPTY_STATUS = "Status can not null";
    private final String code;
    private final String message;
}
