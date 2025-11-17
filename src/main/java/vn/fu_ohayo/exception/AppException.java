package vn.fu_ohayo.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.fu_ohayo.enums.ErrorEnum;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppException extends RuntimeException {

    private String code;
    private List<FieldValidateException> fieldValidateExceptions;

    public AppException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public AppException(String message, String code, List<FieldValidateException> fieldValidateExceptions) {
        super(message);
        this.code = code;
        this.fieldValidateExceptions = fieldValidateExceptions;
    }

}
