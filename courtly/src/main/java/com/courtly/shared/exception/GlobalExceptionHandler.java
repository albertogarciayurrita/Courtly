package com.courtly.shared.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.courtly.auth.exception.InvalidCredentialsException;
import com.courtly.availability.exception.InvalidAvailabilityDateException;
import com.courtly.court.exception.CourtNotFoundException;
import com.courtly.credit.exception.InsufficientCreditsException;
import com.courtly.facility.exception.FacilityNotFoundException;
import com.courtly.facility.exception.InvalidFacilitySchedulerException;
import com.courtly.reservation.exception.InactiveCourtException;
import com.courtly.reservation.exception.InvalidReservationSlotException;
import com.courtly.reservation.exception.ReservationAlreadyCancelledException;
import com.courtly.reservation.exception.ReservationConflictException;
import com.courtly.reservation.exception.ReservationNotFoundException;
import com.courtly.reservation.exception.UnauthorizedReservationCancellationException;
import com.courtly.slot.exception.InvalidBookingSlotException;
import com.courtly.user.exception.UserNotFoundException;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException exception
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException exception
) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
    Map<String, Object> fieldErrors = new HashMap<>();

    exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                    fieldErrors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    )
            );

    ValidationErrorResponse response = new ValidationErrorResponse(
        Instant.now(), status.value(), status.getReasonPhrase(), "Validation failed", fieldErrors);

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

        @ExceptionHandler(FacilityNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleFacilityNotFound(
                FacilityNotFoundException exception
        ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(InvalidFacilitySchedulerException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidFacilitySchedule(
                InvalidFacilitySchedulerException exception
        ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(CourtNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleCourtNotFound(
                CourtNotFoundException exception
        ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(InvalidBookingSlotException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidBookingSlot(
                InvalidBookingSlotException exception
        ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(InvalidAvailabilityDateException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidAvailabilityDate(
                InvalidAvailabilityDateException exception
        ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ApiErrorResponse> handleMissingRequestParameter(
                MissingServletRequestParameterException exception
        ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Required parameter '" + exception.getParameterName()
                        + "' is missing."
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(
                MethodArgumentTypeMismatchException exception
        ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Parameter '" + exception.getName()
                        + "' has an invalid format."
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleUserNotFound(
                UserNotFoundException exception
        ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(InactiveCourtException.class)
        public ResponseEntity<ApiErrorResponse> handleInactiveCourt(
                InactiveCourtException exception
        ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(InvalidReservationSlotException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidReservationSlot(
                InvalidReservationSlotException exception
        ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(ReservationConflictException.class)
        public ResponseEntity<ApiErrorResponse> handleReservationConflict(
                ReservationConflictException exception
        ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(InsufficientCreditsException.class)
        public ResponseEntity<ApiErrorResponse> handleInsufficientCredits(
                InsufficientCreditsException exception
        ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(ReservationNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleReservationNotFound(
                ReservationNotFoundException exception
        ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(UnauthorizedReservationCancellationException.class)
        public ResponseEntity<ApiErrorResponse> handleUnauthorizedReservationCancellation(
                UnauthorizedReservationCancellationException exception
        ) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

        @ExceptionHandler(ReservationAlreadyCancelledException.class)
        public ResponseEntity<ApiErrorResponse> handleReservationAlreadyCancelled(
                ReservationAlreadyCancelledException exception
        ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
        }

}
