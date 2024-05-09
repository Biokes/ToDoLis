package africa.semicolon.utils;

import africa.semicolon.dtos.request.*;
import africa.semicolon.exceptions.InvalidDetails;
import africa.semicolon.exceptions.ToDoListException;

import java.time.LocalDate;
import java.util.Optional;

import static africa.semicolon.exceptions.ExceptionMessages.*;

public class Validator{
    public static String validate(String data){
        if(Optional.ofNullable(data).isEmpty() || Optional.of(data).get().isBlank())
            throw new InvalidDetails(INVALID_DETAILS.getMessage());
        data = data.strip();
        return data.replaceAll("\\s+", " ");
    }
    public static void validateCreateTaskRequest(CreateTaskRequest request){
        request.setTaskTitle(validate(request.getTaskTitle()));
        request.setUsername(validate(request.getUsername()));
        request.setDueDate(cleanDate(request.getDueDate()).toString());
    }
    private static LocalDate cleanDate(String date){
        int output =0;
        try{
            date = date.replace("\\s+","");
            date =date.replaceAll("[^0-9]", "-");
            if(LocalDate.now().isAfter(LocalDate.parse(date))) {
                output = 1;
                throw new ToDoListException(ELAPSED_DATE.getMessage());
            }
            return LocalDate.parse(date);
        }catch(Exception error){
            if(output == 1)
                throw new ToDoListException(ELAPSED_DATE.getMessage());
            throw new ToDoListException(INVALID_DATE.getMessage());
        }
    }
    public static void validateCompleteTaskRequest(CompleteTaskRequest complete){
        complete.setTaskName(validate(complete.getTaskName()));
        complete.setUsername(validate(complete.getUsername()));
    }
    public static void validateStartTaskRequest(StartTaskRequest startRequest){
        startRequest.setTaskName(validate(startRequest.getTaskName()));
        startRequest.setUsername(validate(startRequest.getUsername()));
    }
    public static void validateDeleteTaskRequest(DeleteTaskRequest delete){
        delete.setTaskName(validate(delete.getTaskName()));
        delete.setUsername(validate(delete.getUsername()));
    }
    public static LocalDate validateDate(LocalDate localDate){
        if(localDate.isBefore(LocalDate.now()))
            throw new ToDoListException(ELAPSED_DATE.getMessage());
        return localDate;
    }
    public static void validateUpdate(UpdateTaskRequest update){
        update.setUsername(validate(update.getUsername()));
        update.setNewTitle(validate(update.getNewTitle()));
        update.setOldTitle(validate(update.getOldTitle()));
        if(Optional.ofNullable(update.getDescription()).isPresent()) {
            update.setDescription(validate(update.getDescription()));
        }
    }
    public static void validateAssignTaskRequest(AssignTaskRequest assign){
        assign.setTaskTitle(validate(assign.getTaskTitle()));
        assign.setAssignerUsername(validate(assign.getAssignerUsername()));
        assign.setAssigneeUsername(validate(assign.getAssigneeUsername()));
        assign.setDueDate(cleanDate(assign.getDueDate()).toString());
        assign.setPassword(validate(assign.getPassword()));
        if(Optional.ofNullable(assign.getDescription()).isPresent()){
            assign.setDescription(validate(assign.getDescription()));
            return;
        }
        assign.setDescription("no description saved");
    }
    public static void validateRegister(RegisterRequest request){
        request.setUsername(validate(request.getUsername()));
        request.setPassword(validate(request.getPassword()));
    }
    public static void validateDeleteUer(DeleteUserRequest delete){
        delete.setPassword(validate(delete.getPassword()));
        delete.setUsername(validate(delete.getUsername()));
    }
    public static void validateLogin(LoginRequest login){
        login.setUsername(validate(login.getUsername()));
        login.setPassword(validate(login.getPassword()));
    }
    public static void validateLogOut(LogOut logout){
        logout.setUsername(validate(logout.getUsername()));
        logout.setPassword(validate(logout.getPassword()));
    }
}
