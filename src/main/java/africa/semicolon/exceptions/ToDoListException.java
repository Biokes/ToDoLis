package africa.semicolon.exceptions;

public class ToDoListException extends RuntimeException{
    public ToDoListException(String message){
        super(message);
    }
}
