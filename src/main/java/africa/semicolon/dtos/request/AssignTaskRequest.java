package africa.semicolon.dtos.request;

import lombok.Data;

@Data
public class AssignTaskRequest {
    private String assignerUsername;
    private String taskTitle;
    private String description;
    private String dueDate;
    private String assigneeUsername;
    private String password;
}
