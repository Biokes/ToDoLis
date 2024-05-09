package africa.semicolon.data.model;

import java.time.LocalDateTime;

public enum TaskStatus{
    COMPLETED(LocalDateTime.now()),
    IN_PROGRESS(LocalDateTime.now()),
    PENDING(LocalDateTime.now());
    TaskStatus(LocalDateTime date){
        this.date = date;
    }
    LocalDateTime date ;
    public LocalDateTime getDate(){
        return this.date;
    }
}
