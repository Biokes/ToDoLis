package africa.semicolon.service.implementations;

import africa.semicolon.data.model.TaskStatus;
import africa.semicolon.dtos.request.*;
import africa.semicolon.dtos.response.*;
import africa.semicolon.exceptions.*;
import africa.semicolon.service.inferaces.TaskService;
import africa.semicolon.data.model.Task;
import africa.semicolon.data.repo.TaskRepository;
import africa.semicolon.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static africa.semicolon.exceptions.ExceptionMessages.*;
import static africa.semicolon.utils.Mapper.mapToAssignTask;
import static africa.semicolon.utils.Validator.*;

@Service
public class ToDoTaskService implements TaskService {
    @Autowired
    private TaskRepository repository;
    public void deleteAll(){
        repository.deleteAll();
    }
    public CreateTaskResponse createTask(CreateTaskRequest request){
        validateCreateTaskRequest(request);
        if(isExistingTask(request.getTaskTitle(),request.getUsername()))
            throw new TaskExistsException(TASK_EXISTS.getMessage());
        Task task = Mapper.mapCreateTaskRequestToTask(request);
        task = repository.save(task);
        return Mapper.mapTaskToResponse(task);
    }
    public StartTaskResponse startTaskWith(StartTaskRequest startRequest){
        validateStartTaskRequest(startRequest);
        checkTaskStatus(startRequest);
        Optional<Task> taskFound = repository.findTaskByTaskTitleIgnoreCaseAndUsernameIgnoreCase(startRequest.getTaskName(),
                                                                             startRequest.getUsername());
        validateTask(taskFound);
        taskFound.get().setStatus(TaskStatus.IN_PROGRESS);
        taskFound.get().setDateStarted(taskFound.get()
                                               .getStatus()
                                               .getDate());
        return Mapper.mapTaskToStartTaskResponse(repository.save(taskFound.get()));
    }
    public CompleteTaskResponse completeTask(CompleteTaskRequest complete){
        validateCompleteTaskRequest(complete);
        if(!isExistingTask(complete.getTaskName( ),complete.getUsername()))
            throw new TaskNotFoundException(TASK_NOT_FOUND.getMessage( ));
        if(!isStartedTask(complete.getTaskName(),complete.getUsername()))
            throw new TaskNotStartedException(TASK_NOT_STARTED.getMessage());
        return Mapper.mapToCompleteTaskResponse(complete(complete));
    }
    public void deleteTaskWith(DeleteTaskRequest delete){
        validateDeleteTaskRequest(delete);
        if(!isExistingTask(delete.getTaskName(), delete.getUsername()))
            throw new ToDoListException(TASK_NOT_FOUND.getMessage());
        repository.deleteTaskByUsernameAndTaskTitle(delete.getUsername(),delete.getTaskName());
    }
    public UpdateTaskResponse updateTask(UpdateTaskRequest update){
        validateUpdate(update);
        if(isExistingTask(update.getOldTitle(),update.getUsername())) {
            Task task = findTask(update.getUsername(), update.getOldTitle());
            task.setTaskTitle(update.getNewTitle());
            return Mapper.mapTaskToUpdateResponse(repository.save(task));
        }
        throw new TaskNotFoundException(TASK_NOT_FOUND.getMessage());
    }
    public AssignTaskResponse assignTask(AssignTaskRequest assign){
        validateAssignTaskRequest(assign);
        Task task = repository.save(mapToAssignTask(assign));
        return Mapper.mapToAssignTaskResponse(task);
    }
    public long countTaskByUsername(String username){
        return findAll(username).size();
    }
    public void deleteTasksByUsername(String username){
        repository.deleteAll(findAll(username ));
    }
    public List<CreateTaskResponse> getAllPendingTasks(String username){
        List<Task> allTasks = findAll(username);
        return getPendingTasks(allTasks);
    }
    public List<StartTaskResponse> getAllTasksInProgress(String username){
        List<Task> allTasks = findAll(username);
        return getAllTaskInProgress(allTasks);
    }
    public List<CompleteTaskResponse> getAllCompleteTasks(String username) {
        List<Task> allTasks = findAll(username);
        return getAllCompletedTasks(allTasks);
    }
    public List<AssignedTasksResponse> getallAssignedTasks(String boss) {
        List<Task> allTasks= findAllAssignedTasks(boss);
        return Mapper.mapAllToAssignedTasksResponse(allTasks);
    }
    public Task findTask(String username, String taskTitle){
        return repository.findTaskByTaskTitleIgnoreCaseAndUsernameIgnoreCase(taskTitle,username).get();
    }
    public List<ViewTaskResponse> findAllTask(String username) {
        validate(username);
        List<Task> tasks = findAll(username);
        List<ViewTaskResponse> output = new ArrayList<>();
        tasks.forEach(task->{output.add(Mapper.mapToViewTask(task));});
        return output;
    }
    public List<Task> findAllDueTasks(String username) {
        List<Task> tasks = findAll(username);
        List<Task> output = new ArrayList<>();
        tasks.forEach(task->{if(task.getDueDate().isEqual(LocalDate.now())|| task.getDueDate().isBefore(LocalDate.now())) output.add(task);});
        return output;
    }

    public List<Task>  findAll(String username){
        List<Task> tasks = repository.findAll();
        List<Task> userTask = new ArrayList<>();
        tasks.forEach(task -> {if(task.getUsername().equalsIgnoreCase(username)
                || task.getAssignerUsername().equalsIgnoreCase(username))userTask.add(task);});
        return userTask;
    }
    public void checkTaskExistence(AssignTaskRequest request){
        List<Task> tasks = repository.findAll();
        for(Task task : tasks){
        if(task.getUsername().equalsIgnoreCase(request.getAssigneeUsername())&&
                task.getAssignerUsername().equalsIgnoreCase(request.getAssignerUsername()))
           throw new ToDoListException
           ("you already assigned "+ request.getTaskTitle() + " to " +request.getAssigneeUsername());
        }
    }
    private List<Task> findAllAssignedTasks(String boss){
        List<Task> allTasks= repository.findAll();
        List<Task> assignedTasks = new ArrayList<>();
        allTasks.forEach(task -> {if(task.getAssignerUsername().
                                          equalsIgnoreCase(boss))assignedTasks.add(task);});
       return assignedTasks;
    }
    private List<CompleteTaskResponse> getAllCompletedTasks(List<Task> allTasks) {
        List<Task> completedTasks = new ArrayList<>();
        allTasks.forEach(task -> {if (task.getStatus() == TaskStatus.COMPLETED) completedTasks.add(task);});
        return Mapper.mapAllToCompleteTaskResponses(completedTasks);
    }
    private List<StartTaskResponse> getAllTaskInProgress(List<Task> allTasks){
        List<Task> pendingTasks = new ArrayList<>();
        allTasks.forEach(task -> {if(task.getStatus()== TaskStatus.IN_PROGRESS)pendingTasks.add(task);});
        return Mapper.mapAllToStartTaskResponse(pendingTasks);
    }
    private void checkTaskExistence(Optional<Task> taskFound){
        if(taskFound.isEmpty())
            throw new TaskNotFoundException(TASK_NOT_FOUND.getMessage());
    }
    private void checkTaskStatus(StartTaskRequest startRequest) {
        Optional<Task> task =repository.findTaskByTaskTitleIgnoreCaseAndUsernameIgnoreCase(startRequest.getTaskName(),
                startRequest.getUsername());
        if(task.isPresent() && (task.get().getStatus()== TaskStatus.IN_PROGRESS || task.get().getStatus()== TaskStatus.COMPLETED))
            throw new TaskStartedException(TASK_STARTED.getMessage());
        if(task.isEmpty()) throw new TaskNotFoundException(TASK_NOT_FOUND.getMessage());
    }
    private List<CreateTaskResponse> getPendingTasks(List<Task> allTasks){
        List<Task> pendingTasks = new ArrayList<>();
        allTasks.forEach(task -> {if(task.getStatus()== TaskStatus.PENDING)pendingTasks.add(task);});
        return Mapper.mapAllToCreateTaskResponse(pendingTasks);
    }
    private void validateTask(Optional<Task> taskFound){
        checkTaskExistence(taskFound);
        if (taskFound.get().getStatus() == TaskStatus.IN_PROGRESS)
            throw new TaskStartedException(TASK_STARTED.getMessage());
    }
    private Task complete(CompleteTaskRequest complete){
        Optional<Task> task = repository.findTaskByTaskTitleIgnoreCaseAndUsernameIgnoreCase(complete.getTaskName( ), complete.getUsername( ));
        task.get().setStatus(TaskStatus.COMPLETED);
        return repository.save(task.get());
    }
    private boolean isStartedTask(String taskName, String username){
        Optional<Task> task=repository.findTaskByTaskTitleIgnoreCaseAndUsernameIgnoreCase(taskName, username);
        return task.get().getStatus()== TaskStatus.IN_PROGRESS;
    }
    private boolean isExistingTask(String title,String username){
        List<Task> tasks = repository.findAll();
        for(Task task : tasks){
            if(task.getTaskTitle().equalsIgnoreCase(title)&&task.getUsername().equalsIgnoreCase(username))
               return true;
        }
        return false;
    }
}
