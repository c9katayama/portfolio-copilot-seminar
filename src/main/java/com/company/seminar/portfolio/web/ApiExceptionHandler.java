package com.company.seminar.portfolio.web;

import com.company.seminar.portfolio.ai.AiIntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice(assignableTypes = AiController.class)
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
      MethodArgumentNotValidException exception) {
    FieldError fieldError = exception.getBindingResult().getFieldError();
    String message = fieldError != null ? fieldError.getDefaultMessage() : "Invalid request";
    return ResponseEntity.badRequest().body(new ApiErrorResponse(message));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiErrorResponse> handleStatus(ResponseStatusException exception) {
    return ResponseEntity.status(exception.getStatusCode())
        .body(new ApiErrorResponse(exception.getReason()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException exception) {
    log.error("Configuration error during AI request", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiErrorResponse(exception.getMessage()));
  }

  @ExceptionHandler(AiIntegrationException.class)
  public ResponseEntity<ApiErrorResponse> handleAiIntegration(AiIntegrationException exception) {
    log.error("AI integration request failed", exception);
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(new ApiErrorResponse(exception.getMessage()));
  }
}
