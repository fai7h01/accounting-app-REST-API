package com.cydeo.exception;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.dto.common.DefaultExceptionMessageDto;
import com.cydeo.dto.common.ExceptionWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionWrapper> accessDeniedException(AccessDeniedException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ExceptionWrapper.builder().success(false).code(HttpStatus.FORBIDDEN.value()).message(message).build(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class, BadCredentialsException.class})
    public ResponseEntity<ExceptionWrapper> genericException(Throwable e, HandlerMethod handlerMethod, HttpServletRequest request) {

        Optional<DefaultExceptionMessageDto> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage()) && e.getMessage().isEmpty()) {
            ExceptionWrapper response = ExceptionWrapper
                    .builder()
                    .success(false)
                    .message(defaultMessage.get().getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ExceptionWrapper.builder().success(false).message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).path(request.getRequestURI()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDto defaultExceptionMessageDto = DefaultExceptionMessageDto
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDto);
        }
        return Optional.empty();
    }

//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ExceptionWrapper> userNotFoundExceptionHandler(UserNotFoundException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ExceptionWrapper> userAlreadyExistsExceptionHandler(UserAlreadyExistsException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(UserDoesNotExistsException.class)
//    public ResponseEntity<ExceptionWrapper> userDoesNotExistExceptionHandler(UserDoesNotExistsException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<ExceptionWrapper> productNotFoundExceptionHandler(ProductNotFoundException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(ProductLowLimitAlertException.class)
//    public ResponseEntity<ExceptionWrapper> productLowLimitAlertExceptionHandler(ProductLowLimitAlertException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.OK.value(), message, request.getRequestURI());
//        return ResponseEntity.ok(exceptionWrapper);
//    }
//
//    @ExceptionHandler(CategoryNotFoundException.class)
//    public ResponseEntity<ExceptionWrapper> categoryNotFoundExceptionHandler(CategoryNotFoundException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(CategoryAlreadyExistsException.class)
//    public ResponseEntity<ExceptionWrapper> categoryAlreadyExistsExceptionHandler(CategoryAlreadyExistsException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(CategoryCantBeDeletedException.class)
//    public ResponseEntity<ExceptionWrapper> categoryCantBeDeletedExceptionHandler(CategoryCantBeDeletedException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.CONFLICT.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionWrapper);
//    }
//
//    @ExceptionHandler(CompanyNotFoundException.class)
//    public ResponseEntity<ExceptionWrapper> companyNotFoundExceptionHandler(CompanyNotFoundException exception, HttpServletRequest request){
//        exception.printStackTrace();
//        String message = exception.getMessage();
//        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(HttpStatus.NOT_FOUND.value(), message, request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
//    }
}
