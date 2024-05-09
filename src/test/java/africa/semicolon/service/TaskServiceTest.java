package africa.semicolon.service;
import africa.semicolon.data.model.TaskStatus;
import africa.semicolon.dtos.request.*;
import africa.semicolon.dtos.response.CompleteTaskResponse;
import africa.semicolon.dtos.response.CreateTaskResponse;
import africa.semicolon.dtos.response.StartTaskResponse;
import africa.semicolon.dtos.response.UpdateTaskResponse;
import africa.semicolon.exceptions.*;
import africa.semicolon.service.inferaces.TaskService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TaskServiceTest {
    @Autowired
    private TaskService taskService;
    @BeforeEach
    public void wipe(){
        taskService.deleteAll();
    }
    @Test
    public void CreateTask_TestTaskIsCreated() {
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setTaskTitle("");
        createRequest.setDescription("description");
        assertThrows(InvalidDetails.class, () -> taskService.createTask(createRequest));
        createRequest.setTaskTitle("taskTitle");
        assertThrows(ToDoListException.class, () -> taskService.createTask(createRequest));
        createRequest.setDueDate("2007.12/04");
        assertThrows(ToDoListException.class, () -> taskService.createTask(createRequest));
        createRequest.setDueDate("2024.07/25");
        CreateTaskResponse response = taskService.createTask(createRequest);
        assertEquals("username", response.getUsername());
        assertEquals("taskTitle", response.getTitle());
        assertEquals("description", response.getDescription());
        assertEquals("2024-07-25", response.getDueDate());
        assertEquals(TaskStatus.PENDING, response.getStatus());
    }
    @Test
    public void createTaskTwice_testExceptionIsThrown(){
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setTaskTitle("task title   ");
        createRequest.setDescription("description");
        createRequest.setDueDate("2024[09-19");
        CreateTaskResponse response = taskService.createTask(createRequest);
        assertThrows(TaskExistsException.class, () -> taskService.createTask(createRequest));
        assertEquals("task title", response.getTitle());
    }
    @Test
    public void startTask_testStartIsStarted(){
        StartTaskRequest startRequest = new StartTaskRequest();
        startRequest.setUsername("username");
        startRequest.setTaskName("nylon");
        assertThrows(TaskNotFoundException.class, () -> taskService.startTaskWith(startRequest));
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setTaskTitle("nylon   ");
        createRequest.setDescription("description");
        createRequest.setDueDate("2024'09=27");
        taskService.createTask(createRequest);
        StartTaskResponse response = taskService.startTaskWith(startRequest);
        assertThrows(TaskStartedException.class, () -> taskService.startTaskWith(startRequest));
        assertEquals("username", response.getUsername());
        assertEquals("nylon", response.getTaskTitle());
        assertEquals("description", response.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
    }
    @Test
    public void completeTask_testTaskIsCompleted() throws InterruptedException {
        CompleteTaskRequest complete = new CompleteTaskRequest();
        complete.setTaskName("nylon");
        complete.setUsername("username");
        assertThrows(TaskNotFoundException.class, () -> taskService.completeTask(complete));
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setTaskTitle("nylon   ");
        createRequest.setDescription("description");
        createRequest.setDueDate("2024[07]12");
        taskService.createTask(createRequest);
        assertThrows(TaskNotStartedException.class, () -> taskService.completeTask(complete));
        StartTaskRequest startTask = new StartTaskRequest();
        assertThrows(InvalidDetails.class, () -> taskService.startTaskWith(startTask));
        startTask.setUsername(" username  ");
        assertThrows(InvalidDetails.class, () -> taskService.startTaskWith(startTask));
        startTask.setTaskName("hjewb");
        assertThrows(TaskNotFoundException.class, () -> taskService.startTaskWith(startTask));
        startTask.setTaskName("  nylon  ");
        startTask.setUsername("username");
        taskService.startTaskWith(startTask);
        CompleteTaskResponse completeResponse = taskService.completeTask(complete);
        assertEquals("username", completeResponse.getUsername());
        assertEquals("nylon", completeResponse.getTaskName());
        assertEquals(TaskStatus.COMPLETED, completeResponse.getStatus());
    }
    @Test
    public void deleteTask_testTaskIsDeleted(){
        DeleteTaskRequest delete = new DeleteTaskRequest();
        delete.setTaskName("");
        delete.setUsername("");
        assertThrows(ToDoListException.class, () -> taskService.deleteTaskWith(delete));
        delete.setTaskName("task  ");
        assertThrows(ToDoListException.class, () -> taskService.deleteTaskWith(delete));
        delete.setUsername("username");
        assertThrows(ToDoListException.class, () -> taskService.deleteTaskWith(delete));
        CreateTaskRequest create = new CreateTaskRequest();
        create.setUsername("username");
        create.setTaskTitle("task");
        create.setDueDate("2024;12.31");
        taskService.createTask(create);
        assertThrows(TaskExistsException.class, () -> taskService.createTask(create));
        taskService.deleteTaskWith(delete);
        assertThrows(ToDoListException.class, () -> taskService.deleteTaskWith(delete));
    }
    @Test
    public void updateTaskTitle_testTaskTitleIsUpdated(){
        UpdateTaskRequest update = new UpdateTaskRequest();
        update.setUsername("");
        update.setOldTitle("");
        update.setNewTitle("");
        update.setDescription("");
        assertThrows(ToDoListException.class, () -> taskService.updateTask(update));
        update.setUsername("name");
        update.setNewTitle("1234");
        update.setDescription("my house");
        update.setOldTitle("title");
        assertThrows(ToDoListException.class, () -> taskService.updateTask(update));
        CreateTaskRequest request = new CreateTaskRequest();
        request.setUsername("name");
        request.setTaskTitle("title");
        request.setDueDate("2024.12.12");
        CreateTaskResponse response = taskService.createTask(request);
        assertEquals("name", response.getUsername());
        assertEquals("no description provided", response.getDescription());
        assertEquals(TaskStatus.PENDING, response.getStatus());
        UpdateTaskResponse updateResponse = taskService.updateTask(update);
        assertEquals("1234", updateResponse.getTaskTitle());
    }
    @Test
    public void deleteAllUserTask_testAllUserTaskIsDeleted(){
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setDescription("description");
        createRequest.setTaskTitle("taskTitle");
        createRequest.setDueDate("2024.07/25");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle1");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle2");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle3");
        taskService.createTask(createRequest);
        assertEquals(4, taskService.countTaskByUsername("username"));
        taskService.deleteTasksByUsername("username");
        assertEquals(0, taskService.countTaskByUsername("username"));
    }
    @Test
    public void findAllStartedTask_testAllStartedTaskAreFound(){
        assertEquals(0, taskService.getAllPendingTasks("username").size());
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setDescription("description");
        createRequest.setTaskTitle("taskTitle");
        createRequest.setDueDate("2024.07/25");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle1");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle2");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle3");
        taskService.createTask(createRequest);
        assertEquals(4, taskService.getAllPendingTasks("username").size());
    }
    @Test
    public void findAllTaskInProgress_testAllTaskInProgressIsFound(){
        assertEquals(0, taskService.getAllPendingTasks("username").size());
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setDescription("description");
        createRequest.setTaskTitle("taskTitle");
        createRequest.setDueDate("2024.07/25");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle1");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle2");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle3");
        taskService.createTask(createRequest);
        assertEquals(4, taskService.getAllPendingTasks("username").size());
        StartTaskRequest startRequest = new StartTaskRequest();
        startRequest.setUsername("username");
        startRequest.setTaskName("taskTitle");
        taskService.startTaskWith(startRequest);
        startRequest.setTaskName("taskTitle1");
        taskService.startTaskWith(startRequest);
        startRequest.setTaskName("taskTitle3");
        taskService.startTaskWith(startRequest);
        assertEquals(1, taskService.getAllPendingTasks("username").size());
        assertEquals(3, taskService.getAllTasksInProgress("username").size());
    }
    @Test
    public void findAllCompleted_testAllTaskCompletedIsCompleted(){
        assertEquals(0, taskService.getAllCompleteTasks("username").size());
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setDescription("description");
        createRequest.setTaskTitle("taskTitle");
        createRequest.setDueDate("2024.07/25");
        taskService.createTask(createRequest);
        createRequest.setTaskTitle("taskTitle1");
        taskService.createTask(createRequest);
        StartTaskRequest startRequest = new StartTaskRequest();
        startRequest.setUsername("username");
        startRequest.setTaskName("taskTitle");
        taskService.startTaskWith(startRequest);
        startRequest.setTaskName("taskTitle1");
        taskService.startTaskWith(startRequest);
        CompleteTaskRequest complete = new CompleteTaskRequest();
        complete.setTaskName("taskTitle");
        complete.setUsername("username");
        taskService.completeTask(complete);
        assertEquals(1, taskService.getAllCompleteTasks("username").size());
    }
}