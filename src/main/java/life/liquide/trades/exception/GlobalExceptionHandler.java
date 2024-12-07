package life.liquide.trades.exception;

import life.liquide.trades.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceResponse<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ServiceResponse<>(ServiceResponse.FAILED, ex.getMessage(), null);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceResponse<String> handleRuntimeException(RuntimeException ex) {
        return new ServiceResponse<>(ServiceResponse.FAILED, "An unexpected error occurred: " + ex.getMessage(), null);
    }

}
