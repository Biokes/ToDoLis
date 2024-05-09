package africa.semicolon.dtos.response;

import africa.semicolon.data.model.TaskStatus;
import lombok.Data;
import org.springframework.context.annotation.Lazy;

@Data
public class CreateTaskResponse{
    private String username;
    private String title;
    private String description;
    private TaskStatus status;
    private String dateCreated;
    private String dueDate;
    private String assignerUsername;
}
