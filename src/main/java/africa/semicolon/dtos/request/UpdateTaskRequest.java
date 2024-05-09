package africa.semicolon.dtos.request;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String username;
    private String oldTitle;
    private String newTitle;
    private String description;
    private String password;
}
