package africa.semicolon.controller;


import africa.semicolon.dtos.request.*;
import africa.semicolon.dtos.response.ApiResponse;
import africa.semicolon.dtos.response.CompleteTaskResponse;
import africa.semicolon.dtos.response.CreateTaskResponse;
import africa.semicolon.dtos.response.ViewTaskResponse;
import africa.semicolon.service.inferaces.AppService;
import africa.semicolon.exceptions.ToDoListException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api/v1/toDoList")
public class ToDoListController{
    @Autowired
    private AppService appService;
    @PostMapping("/Register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try{
            appService.register(request);
            return new ResponseEntity<>(new ApiResponse(true, "Registered successfully"), CREATED);
        }
        catch(ToDoListException exception){
            return new ResponseEntity<>(new ApiResponse(false,exception.getMessage()), BAD_REQUEST);
        }
    }
    @DeleteMapping("/Delete_account")
    public ResponseEntity<?> deActivateAccount(@RequestBody DeleteUserRequest delete){
        try{
            appService.deleteAccount(delete);
            return new ResponseEntity<>(new ApiResponse(true,delete.getUsername()+"deleted successfully"),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),BAD_REQUEST);

        }
    }
    @PutMapping("/Create-task")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest create){
        try{
            return new ResponseEntity<>(new ApiResponse(true,appService.createTask(create)), CREATED);
        }
        catch(ToDoListException exception){
            return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()),BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete_task")
    public ResponseEntity<?> deleteTask(@RequestBody DeleteTaskRequest delete){
        try{
            appService.deleteTask(delete);
            return new ResponseEntity<>((new ApiResponse(false,delete.getTaskName()+" deleted successfully.")),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),BAD_REQUEST);
        }
    }
    @PatchMapping("/update-task")
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskRequest update){
        try{
            return new ResponseEntity<>(new ApiResponse(true,appService.updateTask(update)),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),BAD_REQUEST);
        }
    }
    @PatchMapping("/Complete-Task")
    public ResponseEntity<?> completeTask(@RequestBody CompleteTaskRequest complete){
        try{
           return new ResponseEntity<>(new ApiResponse(true,appService.completeTask(complete)),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
    @PatchMapping("/start-task")
    public ResponseEntity<?> startTask(@RequestBody StartTaskRequest startTaskRequest){
        try{
            return new ResponseEntity<>(new ApiResponse(true, appService.startTask(startTaskRequest)), OK);
        }catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
    @PostMapping("/assign-Task")
    public ResponseEntity<?> assignTask(@RequestBody AssignTaskRequest assign){
        try{
            return new ResponseEntity<>(new ApiResponse(true,appService.assignTask(assign)),CREATED);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
    @GetMapping("/logIn")
    public ResponseEntity<?> login(@RequestBody LoginRequest login){
        try{
            return new ResponseEntity<>(new ApiResponse(true,appService.login(login)),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),BAD_REQUEST);
        }
    }
    @GetMapping("/notifications")
    public ResponseEntity<?> notifications(@RequestBody LoginRequest login){
        try{
            return new ResponseEntity<>(new ApiResponse(true,appService.viewNotifications    (login)),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false, error.getMessage()),BAD_REQUEST);
        }
    }
    @GetMapping("/created-task-list")
    public ResponseEntity<?> checkAllTaskCreated(@RequestBody LoginRequest login){
        try{
            return new ResponseEntity<>(new ApiResponse(true,appService.findAllTask(login)),OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
    @GetMapping("/checkPendingTask")
    public ResponseEntity<?> checkPendingTasks(@RequestBody LoginRequest login){
        try{
            List<CreateTaskResponse> responsne = appService.getAllPendingTask(login);
            Object result = responsne.isEmpty() ? "no pending task yet" : responsne;
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
    @GetMapping("/check-completed-tasks")
    public ResponseEntity<?> checkCompletedTasks(@RequestBody LoginRequest login){
        try{
            List<CompleteTaskResponse> responsne = appService.getAllCompleteTask(login);
            Object result = responsne.isEmpty() ? "no completed task yet" : responsne;
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
    @GetMapping("/get-all-assigned-tasks")
    public ResponseEntity<?> getAllAssignedTasks(@RequestBody LoginRequest login){
        try{
            List<ViewTaskResponse> responsne = appService.getAllAssignedTask(login);
            Object result = responsne.isEmpty() ? "no completed task yet" : responsne;
            return new ResponseEntity<>(new ApiResponse(true, result), OK);
        }
        catch(ToDoListException error){
            return new ResponseEntity<>(new ApiResponse(false,error.getMessage()),BAD_REQUEST);
        }
    }
}
// get notifications from due tasks not completed