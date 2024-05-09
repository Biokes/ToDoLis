package africa.semicolon.data.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notifications {
    private String notification;
    private String taskTitle;
    private LocalDateTime timeCreated= LocalDateTime.now();
    private boolean isSeen;
}
