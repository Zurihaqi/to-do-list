package enigma.to_do_list.utils.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import enigma.to_do_list.model.Task;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class TaskDto {

    @Getter
    @Setter
    public static class request {
        private String title;
        private String description;

        private String dueDate;

        private Task.Status status;
    }

    @Getter
    @Setter
    @Builder
    public static class response {
        private Integer id;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer userId;
        private String title;
        private String description;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private OffsetDateTime dueDate;

        private Task.Status status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private OffsetDateTime createdAt;
    }
}
