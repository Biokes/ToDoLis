package africa.semicolon.dtos.response;

import africa.semicolon.data.model.TaskStatus;
import lombok.Data;
@Data
public class UpdateTaskResponse {
    private String username;
    private String taskTitle;
    private String dueDate;
    private TaskStatus status;
}
