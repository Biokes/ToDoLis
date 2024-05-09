package africa.semicolon.utils;

import africa.semicolon.data.model.Task;
import africa.semicolon.data.model.TaskStatus;
import africa.semicolon.data.model.User;
import africa.semicolon.dtos.response.*;
import africa.semicolon.data.model.Notifications;
import africa.semicolon.dtos.request.AssignTaskRequest;
import africa.semicolon.dtos.request.CreateTaskRequest;
import africa.semicolon.dtos.request.RegisterRequest;
import africa.semicolon.exceptions.InvalidDetails;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static africa.semicolon.exceptions.ExceptionMessages.INVALID_DATE;
import static africa.semicolon.utils.Validator.validateAssignTaskRequest;

public class Mapper{
    public static Task mapCreateTaskRequestToTask(CreateTaskRequest request){
    Task task = new Task();
    task.setUsername(request.getUsername());
    task.setTaskTitle(request.getTaskTitle());
    task.setDescription(request.getDescription());
    task.setStatus(TaskStatus.PENDING);
    task.setAssignerUsername("self");
    task.setDueDate(Validator.validateDate(convertToDate(request.getDueDate())));
    task.setDateCreated(task.getStatus().getDate().toString());
    return task;
    }
    public static LocalDate convertToDate(String dateGiven){
        try{
            dateGiven = dateGiven.replaceAll("\\s+", "/");
            dateGiven = dateGiven.replaceAll("[^0-9]", "-");
            return LocalDate.parse(dateGiven, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        catch (Exception exception){
            throw new InvalidDetails(INVALID_DATE.getMessage());
        }
    }
    public static CreateTaskResponse mapTaskToResponse(Task task){
        CreateTaskResponse response = new CreateTaskResponse();
        response.setStatus(task.getStatus());
        response.setDescription(setDescription(task.getDescription()));
        response.setUsername(task.getUsername());
        response.setTitle(task.getTaskTitle());
        response.setDateCreated(task.getDateCreated());
        response.setDueDate(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.setStatus(task.getStatus());
        if(Optional.ofNullable(task.getAssignerUsername()).isPresent()){
            response.setAssignerUsername(task.getAssignerUsername());
            return response;
        }
        response.setAssignerUsername("self");
        return response;
    }
    public static StartTaskResponse mapTaskToStartTaskResponse(Task task){
        StartTaskResponse response = new StartTaskResponse();
        response.setTaskTitle(task.getTaskTitle());
        response.setUsername(task.getUsername());
        response.setStatus(task.getStatus());
        task.setDateStarted(task.getStatus().getDate());
        if(Optional.ofNullable(task.getDescription()).isEmpty() || task.getDescription().isBlank())
            response.setDescription("\"nothing saved as description\"");
        else response.setDescription(task.getDescription());
        return response;
    }
    public static CompleteTaskResponse mapToCompleteTaskResponse(Task complete){
        CompleteTaskResponse response = new CompleteTaskResponse();
        response.setTaskName(complete.getTaskTitle());
        response.setUsername(complete.getUsername());
        response.setStatus(complete.getStatus());
        response.setDateCreated(complete.getDateCreated());
        response.setDuration(getDuration(complete.getDateStarted().toString()));
        if(Optional.ofNullable(complete.getAssignerUsername()).isEmpty())
            response.setAssignerUsername(complete.getUsername());
        else response.setAssignerUsername(complete.getAssignerUsername());
        LocalDateTime date = complete.getDateStarted();
        response.setStartDate(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a")));
        return response;
    }
    public static UpdateTaskResponse mapTaskToUpdateResponse(Task task){
        UpdateTaskResponse response = new UpdateTaskResponse();
        response.setUsername(task.getUsername());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.setTaskTitle(task.getTaskTitle());
        return response;
    }
    public static Task mapToAssignTask(AssignTaskRequest assign){
        validateAssignTaskRequest(assign);
        Task task = new Task();
        task.setUsername(assign.getAssigneeUsername());
        task.setDueDate(convertToDate(assign.getDueDate()));
        task.setDescription(assign.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setTaskTitle(assign.getTaskTitle());
        task.setAssignerUsername(assign.getAssignerUsername());
        task.setDateCreated(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
        return task;
    }
    public static AssignTaskResponse mapToAssignTaskResponse(Task task){
        AssignTaskResponse response = new AssignTaskResponse();
        response.setStatus(task.getStatus());
        response.setTaskTitle(task.getTaskTitle());
        String date = task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        response.setDueDate(date);
        response.setAssigneeUsername(task.getUsername());
        response.setDescription(task.getDescription());
        if(Optional.ofNullable(task.getStatus()).isEmpty()){
            response.setAssignerUsername("not an assigned task");
            return response;
        }
        response.setAssignerUsername(task.getAssignerUsername());
        return response;
    }
    public static List<StartTaskResponse> mapAllToStartTaskResponse(List<Task> pendingTasks) {
        List<StartTaskResponse> response = new ArrayList<>();
        pendingTasks.forEach(task -> {response.add(mapTaskToStartTaskResponse(task));});
        return response;
    }
    public static List<CompleteTaskResponse> mapAllToCompleteTaskResponses(List<Task> completedTasks) {
        List<CompleteTaskResponse> responseList = new ArrayList<>();
        completedTasks.forEach(task -> {responseList.add(mapToCompleteTaskResponse(task));});
        return responseList;
    }
    public static List<CreateTaskResponse> mapAllToCreateTaskResponse(List<Task> pendingTasks) {
        List<CreateTaskResponse> response = new ArrayList<>();
        pendingTasks.forEach(task -> {response.add(mapTaskToResponse(task));});
        return response;
    }
    public static List<AssignedTasksResponse> mapAllToAssignedTasksResponse(List<Task> allTasks){
        List<AssignedTasksResponse> output = new ArrayList<>();
        allTasks.forEach(task->{output.add(Mapper.mapToAssignedTasksResponse(task));});
        return output;
    }
    private static AssignedTasksResponse mapToAssignedTasksResponse(Task task){
        AssignedTasksResponse response = new AssignedTasksResponse();
        response.setUsername(task.getAssignerUsername());
        response.setAssigneeUsername(task.getUsername());
        response.setStatus(task.getStatus());
        response.setDateAssigned(task.getDateCreated());
        response.setDueDate(task.getDueDate().toString());
        response.setTitle(task.getTaskTitle());
        response.setDescription(task.getDescription());
        if(Optional.ofNullable(task.getDateStarted()).isPresent()) {
            response.setDateStarted(task.getDateStarted().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")));
            return response;
        }
        response.setDateStarted("not started yet");
        return response;
    }
    private static String getDuration(String status){
        Duration time = Duration.between(LocalDateTime.parse(status),LocalDateTime.now());
        return String.format("%s days, %s hours, %s minutes, %s seconds", time.toDays()%24,
                time.toHours()%(60),
                time.toMinutes()%(60),
                time.toSeconds()%60);
    }
    private static String setDescription(String given){
        Optional<String> string = Optional.ofNullable(given);
        if(string.isEmpty())
            given= "no description provided";
        return given;
    }
    public static User mapToUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNotifications(new ArrayList<>());
        user.setLoggedIn(true);
        return user;
    }
    public static Notifications mapAssignTaskToNotification(AssignTaskRequest assignTaskRequest){
        Notifications notification = new Notifications();
        notification.setNotification(assignTaskRequest.getAssignerUsername() + "Assigned you a task");
        notification.setTimeCreated(LocalDateTime.now());
        notification.setTaskTitle(assignTaskRequest.getTaskTitle());
        return notification;
    }
    public static LoginResponse mapUserToLogInResponse(User user){
        LoginResponse response = new LoginResponse();
        response.setNotification(mapNotificationsToNotifier(user.getNotifications()));
        return response;
    }
    private static List<Notifier> mapNotificationsToNotifier(List<Notifications> notification){
        List<Notifier> output = new ArrayList<>();
        Notifier notifier = new Notifier();
        notification.forEach(note->{
            notifier.setDescription(note.getNotification());
            notifier.setTaskTitle(note.getTaskTitle());
            notifier.setTimeNotified(note.getTimeCreated().
                    format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
            output.add(notifier);
        });
        return output;
    }
    public static ViewTaskResponse mapToViewTask(Task task) {
        ViewTaskResponse response = new ViewTaskResponse();
        response.setDescription(task.getDescription());
        response.setTaskTitle(task.getTaskTitle());
        response.setDateStarted(task.getDateStarted() != null ? task.getDateStarted().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")) : "task not started");
        response.setDateCreated(task.getDateCreated());
        response.setDueDate(task.getDueDate());
        response.setStatus(task.getStatus());
        response.setUsername(task.getUsername());
        response.setAssignerUsername(task.getAssignerUsername());
        if(Optional.ofNullable(task.getDateStarted()).isEmpty()) {
            response.setDateStarted("not started yet");
            return response;
        }
        response.setDateStarted(task.getDateStarted().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
        return response;
    }
    public static List<Notifications> mapToNotifications(List<Task> tasks) {
        List<Notifications> output = new ArrayList<>();
        Notifications notifier = new Notifications();
        tasks.forEach(task -> {
            notifier.setNotification(task.getTaskTitle() + " Due date : " + task.getDueDate());
            notifier.setTaskTitle(task.getTaskTitle());
            notifier.setTimeCreated(LocalDateTime.now());
            output.add(notifier);
        });
        return output;
    }

    public static List<Notifier> mapToUserNotification(List<Notifications> notifications) {
        return mapNotificationsToNotifier(notifications);
    }
}
