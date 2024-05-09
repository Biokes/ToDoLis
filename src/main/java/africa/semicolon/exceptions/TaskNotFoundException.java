package africa.semicolon.exceptions;

public class TaskNotFoundException extends ToDoListException{
    public TaskNotFoundException(String message){
        super(message);
    }
}
