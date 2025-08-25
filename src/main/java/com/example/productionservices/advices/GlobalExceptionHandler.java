package com.example.productionservices.advices;

//import com.example.employeeservice.exceptions.ResourceNotFoundException;
import com.example.productionservices.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException exception){
      ApiError apiError=ApiError
              .builder()
              .status(HttpStatus.NOT_FOUND)
              .message(exception.getMessage())
              .build();
//      return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        return buildErrorResponseEntity(apiError);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED) // 401 is correct for login failure
                .message(ex.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException ex) {
        ApiError apiError=ApiError
                .builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }


    private ResponseEntity<ApiResponse> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse(apiError),apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleInternalServerError(Exception exception){
        ApiError apiError=ApiError
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception){
        ApiError apiError=ApiError
                .builder()
                .status(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<String> errors=exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ApiError apiError=ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("input validation failed")
                .suberrors(errors)
                .build();
        return buildErrorResponseEntity(apiError);

    }

}
