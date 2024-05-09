package africa.semicolon.dtos.request;

import lombok.Data;

@Data
public class StartTaskRequest{
    private String taskName;
    private String username;
    private String password;
}
