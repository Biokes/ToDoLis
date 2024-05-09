package africa.semicolon.dtos.response;

import lombok.Data;

@Data
public class Notifier {
    private String description;
    private String taskTitle;
    private String timeNotified;
}
