package enigma.to_do_list.utils.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static <T> ResponseEntity<?> renderJson(T data, String message, HttpStatus httpStatus) {
        WebResponse<T> response = WebResponse.<T>builder()
                .message(message)
                .status(httpStatus.getReasonPhrase())
                .data(data)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<?> renderJson(String message, HttpStatus httpStatus) {
        WebResponse<T> response = WebResponse.<T>builder()
                .message(message)
                .status(httpStatus.getReasonPhrase())
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<?> renderError(String message, HttpStatus httpStatus, List<T> errors) {
        WebResponseError<T> response = WebResponseError.<T>builder()
                .message(message)
                .status(httpStatus.getReasonPhrase())
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<?> renderJson(T data, String message) {
        return renderJson(data, message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<?> renderJson(T data) {
        return renderJson(data, "Success");
    }

}
