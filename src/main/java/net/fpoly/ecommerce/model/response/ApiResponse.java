package net.fpoly.ecommerce.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean isSuccess;
    private T data;
    private Object errors;
    private LocalDateTime timestamp;
    private int status;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .isSuccess(true)
                .timestamp(LocalDateTime.now())
                .status(200)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        Map<String, String> errors = new HashMap<>();
        errors.put(errorCode, errorMessage);;
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }


    public static <T> ApiResponse<T> errorBindingResult(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ApiResponse.<T>builder()
                .isSuccess(false)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
