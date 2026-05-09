package com.autonetconfig.lite.api;

import com.autonetconfig.lite.dto.ErrorResponse;
import com.autonetconfig.lite.service.DeviceLockedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(DeviceLockedException.class)
    public ResponseEntity<ErrorResponse> handleDeviceLocked(DeviceLockedException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("DEVICE_LOCKED", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("INVALID_CONFIG", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("INVALID_REQUEST", "deviceId, configChange, and changeType are required."));
    }
}
