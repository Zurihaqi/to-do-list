package enigma.to_do_list.exception;

import enigma.to_do_list.utils.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return Response.renderError(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return Response.renderError(ex,"Invalid email or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedRoleException.class)
    public ResponseEntity<?> handleUnauthorizedRoleException(UnauthorizedRoleException ex) {
        return Response.renderError(ex, ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<?> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        return Response.renderError(ex, ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CredAlreadyExistException.class)
    public ResponseEntity<?> handleCredAlreadyExistException(CredAlreadyExistException ex) {
        return Response.renderError(ex, ex.getMessage(), HttpStatus.CONFLICT);
    }
}
