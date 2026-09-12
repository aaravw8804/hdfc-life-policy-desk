package com.hdfclife.desk.config;

import com.hdfclife.desk.exception.ClaimNotFoundException;
import com.hdfclife.desk.exception.DuplicatePolicyException;
import com.hdfclife.desk.exception.InvalidClaimException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePolicyNotFound(PolicyNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleClaimNotFound(ClaimNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DuplicatePolicyException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatePolicy(DuplicatePolicyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<Map<String, String>> handleInvalidClaim(InvalidClaimException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}
