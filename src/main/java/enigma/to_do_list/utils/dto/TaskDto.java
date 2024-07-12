package enigma.to_do_list.utils.dto;

import lombok.*;

import java.util.Date;

public class TaskDto {

    @Getter
    @Setter
    public static class request {
        private Integer user_id;
        private String task;
    }

    @Getter
    @Setter
    @Builder
    public static class response {
        private Integer user_id;
        private String task;
        private boolean completed;
        private Date createdAt;
        private Date completedAt;
    }
}
