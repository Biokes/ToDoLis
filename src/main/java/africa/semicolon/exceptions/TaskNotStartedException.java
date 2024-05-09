package africa.semicolon.exceptions;

public class TaskNotStartedException extends ToDoListException{
    public TaskNotStartedException(String message){
        super(message);
    }
}
