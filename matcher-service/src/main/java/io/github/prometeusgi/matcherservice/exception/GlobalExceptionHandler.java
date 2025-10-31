package io.github.prometeusgi.matcherservice.exception;

import io.github.prometeusgi.matcherservice.client.exception.AiServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FIELD = "field";
    private static final String KEY_OBJECT = "object";
    private static final String PROP_ERRORS = "errors";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, request,
                "Validation failed",
                "One or more fields have invalid values.");

        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        KEY_FIELD, error.getField(),
                        KEY_MESSAGE, error.getDefaultMessage()
                ))
                .toList();

        List<Map<String, String>> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> Map.of(
                        KEY_OBJECT, error.getObjectName(),
                        KEY_MESSAGE, error.getDefaultMessage()
                ))
                .toList();

        pd.setProperty(PROP_ERRORS, !globalErrors.isEmpty()
                ? Stream.concat(fieldErrors.stream(), globalErrors.stream()).toList()
                : fieldErrors);

        return respond(HttpStatus.BAD_REQUEST, pd);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, request,
                "Validation failed",
                "One or more parameters have invalid values.");

        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        KEY_FIELD, resolveViolationProperty(violation),
                        KEY_MESSAGE, violation.getMessage()
                ))
                .toList();
        pd.setProperty(PROP_ERRORS, errors);

        return respond(HttpStatus.BAD_REQUEST, pd);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadable(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, request,
                "Malformed request",
                "Request body is missing or malformed JSON.");
        return respond(HttpStatus.BAD_REQUEST, pd);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String detail = "Parameter '" + ex.getName() + "' has an invalid value" +
                (ex.getValue() != null ? ": '" + ex.getValue() + "'" : "") +
                ", expected " + expected + ".";
        ProblemDetail pd = buildProblem(HttpStatus.BAD_REQUEST, request,
                "Invalid parameter",
                detail);
        return respond(HttpStatus.BAD_REQUEST, pd);
    }

    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<ProblemDetail> handleAiService(AiServiceException ex,
                                                         HttpServletRequest request) {
        log.error("AI service call failed: {}", ex.getMessage(), ex);
        ProblemDetail pd = buildProblem(HttpStatus.BAD_GATEWAY, request,
                "Upstream service error",
                "Failed to process request due to AI service error.");
        return respond(HttpStatus.BAD_GATEWAY, pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleRuntime(Exception ex,
                                                       HttpServletRequest request) {
        log.error("Unhandled server error", ex);
        ProblemDetail pd = buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, request,
                "Internal server error",
                "An unexpected error occurred. Please try again later.");
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, pd);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ProblemDetail> handleNotFound(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = buildProblem(HttpStatus.NOT_FOUND, request,
                "Resource not found",
                "The requested resource was not found.");
        return respond(HttpStatus.NOT_FOUND, pd);
    }

    private static String resolveViolationProperty(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "";
        return path.isBlank() ? "parameter" : path;
    }

    private static ProblemDetail buildProblem(HttpStatus status,
                                              HttpServletRequest request,
                                              String title,
                                              String detail) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        pd.setDetail(detail);
        String url = request.getRequestURL().toString();
        String qs = request.getQueryString();
        pd.setInstance(URI.create(qs == null ? url : url + "?" + qs));
        return pd;
    }

    private static ResponseEntity<ProblemDetail> respond(HttpStatus status, ProblemDetail pd) {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(pd);
    }
}
