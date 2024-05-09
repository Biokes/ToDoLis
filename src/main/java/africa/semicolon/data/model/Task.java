package africa.semicolon.data.model;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document("Tasks")
public class Task{
    @Id
    private String id;
    private String username;
    private String description;
    private String taskTitle;
    private TaskStatus status;
    private String dateCreated;
    private LocalDateTime dateStarted;
    private LocalDate dueDate;
    private String assignerUsername;

}
