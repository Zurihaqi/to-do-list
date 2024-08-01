package enigma.to_do_list.utils.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import enigma.to_do_list.model.Task;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;

public class TaskDto {

    @Getter
    @Setter
    public static class request {
        private String title;
        private String description;
        @Temporal(TemporalType.DATE)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private Date dueDate;
        private Task.Status status;
    }

    @Getter
    @Setter
    @Builder
    public static class response {
        private Integer id;
        private String title;
        private String description;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private Date dueDate;
        private Task.Status status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private Date createdAt;
    }
}
