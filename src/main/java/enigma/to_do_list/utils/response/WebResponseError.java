package enigma.to_do_list.utils.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WebResponseError<T> {
    private String message;
    private String error;
}
