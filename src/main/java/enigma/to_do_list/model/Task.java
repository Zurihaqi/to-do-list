package enigma.to_do_list.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Status status;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime dueDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Status {
            PENDING,
            IN_PROGRESS,
            COMPLETED
    }
}
