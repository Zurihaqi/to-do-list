package enigma.to_do_list.exception;

import enigma.to_do_list.utils.response.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return Response.renderError(ex.getMessage(), HttpStatus.BAD_REQUEST, Arrays.asList(ex.getStackTrace()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return Response.renderError("Invalid email or password", HttpStatus.UNAUTHORIZED, Arrays.asList(ex.getStackTrace()));
    }
}
