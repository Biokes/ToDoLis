package africa.semicolon.dtos.response;

import africa.semicolon.data.model.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ViewTaskResponse {
    private String username;
    private String description;
    private String taskTitle;
    private TaskStatus status;
    private String dateCreated;
    private String dateStarted;
    private LocalDate dueDate;
    private String assignerUsername;
}
