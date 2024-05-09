package africa.semicolon.exceptions;

public class TaskExistsException extends ToDoListException{
    public TaskExistsException(String message){
        super(message);
    }
}
