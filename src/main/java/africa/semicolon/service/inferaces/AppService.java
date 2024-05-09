package africa.semicolon.service.inferaces;

import africa.semicolon.dtos.request.*;
import africa.semicolon.dtos.response.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppService {
    void deleteAll();
    void register(RegisterRequest register);
    long countAllUsers();
    CreateTaskResponse createTask(CreateTaskRequest createTaskRequest);
    void deleteAccount(DeleteUserRequest deleteRequest);
    long countAllUserTask(String username);
    void deleteTask(DeleteTaskRequest deleteTaskRequest);
    UpdateTaskResponse updateTask(UpdateTaskRequest updateTask);
    StartTaskResponse startTask(StartTaskRequest startTaskRequest);
    CompleteTaskResponse completeTask(CompleteTaskRequest completeTaskRequest);
    AssignTaskResponse assignTask(AssignTaskRequest assignTaskRequest);
    LoginResponse login(LoginRequest login);
    void logOut(LogOut logout);
    List<ViewTaskResponse> findAllTask(LoginRequest login);
    List<CompleteTaskResponse> getAllCompleteTask(LoginRequest login);
    List<ViewTaskResponse> getAllTaskNotCompleted(LoginRequest login);
    List<CreateTaskResponse> getAllPendingTask(LoginRequest login);
    List<ViewTaskResponse> getAllAssignedTask(LoginRequest login);
    List<Notifier> viewNotifications(LoginRequest login);
//    List<Notifier> getUserNotifications(LoginRequest login);
}
