package com.sau.swe.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private static  MessageSource messageSource;
    private boolean success;
    private String message;
    private T data;

    public static <T> GenericResponse<T> success() {
        return success(null);
    }

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .message("SUCCESS!")
                .data(data)
                .success(true)
                .build();
    }
    public static <T> GenericResponse<T> success(String messageKey) {
        return GenericResponse.<T>builder()
                .message(getMessage(messageKey))
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> success(T data, String messageKey) {
        return GenericResponse.<T>builder()
                .message(getMessage(messageKey))
                .data(data)
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> error() {
        return GenericResponse.<T>builder()
                .message("ERROR!")
                .success(false)
                .build();
    }
    public static <T> GenericResponse<T> error(String messageKey) {
        return GenericResponse.<T>builder()
                .message(getMessage(messageKey))
                .success(false)
                .build();
    }
    private static String getMessage(String messageKey){
        return messageSource.getMessage(messageKey,null, LocaleContextHolder.getLocale());
    }

    public static void setMessageSource(MessageSource messageSource) {
        GenericResponse.messageSource = messageSource;
    }
}
