package africa.semicolon.service;


import africa.semicolon.data.model.TaskStatus;
import africa.semicolon.dtos.request.*;
import africa.semicolon.dtos.response.*;
import africa.semicolon.exceptions.ToDoListException;
import africa.semicolon.service.inferaces.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ToDoListServicesTest {
    @Autowired
    private AppService appService;
    @BeforeEach
    void wipe(){
        appService.deleteAll();
    }
    @Test
    public void testUserRegisteration(){
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password");
        appService.register(register);
        assertEquals(1,appService.countAllUsers());
        assertThrows(ToDoListException.class,()->appService.register(register));
    }
    @Test
    public void registerAndCreateTaskTest(){
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password");
        appService.register(register);
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("name");
        createRequest.setDueDate("2021-04-21");
        createRequest.setTaskTitle("");
        assertThrows(ToDoListException.class,()->appService.createTask(createRequest));
        createRequest.setDueDate("2024-08-21");
        createRequest.setTaskTitle("task1");
        assertThrows(ToDoListException.class,()->appService.createTask(createRequest));
        createRequest.setUsername("username");
        CreateTaskResponse createResponse = appService.createTask(createRequest);
        assertEquals("username",createRequest.getUsername());
        assertEquals("task1", createResponse.getTitle());
        assertEquals(TaskStatus.PENDING,createResponse.getStatus());
    }
    @Test
    public void deleteAccount_testAccountAndTaskAreDeleted(){
        DeleteUserRequest deleteRequest = new DeleteUserRequest();
        deleteRequest.setUsername("username");
        deleteRequest.setPassword("password");
        assertThrows(ToDoListException.class,()-> appService.deleteAccount(deleteRequest));
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password1");
        appService.register(register);
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setUsername("username");
        createRequest.setTaskTitle("title");
        createRequest.setDueDate("2014.07.25");
        createRequest.setDescription("description");
        assertThrows(ToDoListException.class,()->appService.createTask(createRequest));
        createRequest.setDueDate("2024.07.25");
        appService.createTask(createRequest);
        createRequest.setTaskTitle("title1");
        appService.createTask(createRequest);
        createRequest.setTaskTitle("title2");
        appService.createTask(createRequest);
        assertEquals(3,appService.countAllUserTask(deleteRequest.getUsername()));
        assertThrows(ToDoListException.class,()->appService.deleteAccount(deleteRequest));
        deleteRequest.setPassword("password1");
        appService.deleteAccount(deleteRequest);
        assertEquals(0,appService.countAllUsers());
        assertThrows(ToDoListException.class,()->appService.countAllUserTask(deleteRequest.getUsername()));

    }
    @Test
    public void deleteTask_testTaskIsDeleted(){
        DeleteTaskRequest deleteTaskRequest = new DeleteTaskRequest();
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password");
        appService.register(register);
        assertThrows(ToDoListException.class,()->appService.deleteTask(deleteTaskRequest));
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setUsername("usernam");
        createTaskRequest.setTaskTitle("task1");
        createTaskRequest.setDueDate("2023-07-09");
        assertThrows(ToDoListException.class, ()->appService.createTask(createTaskRequest));
        createTaskRequest.setUsername("username");
        assertThrows(ToDoListException.class,()->appService.createTask(createTaskRequest));
        createTaskRequest.setDueDate("2024-06-10");
        appService.createTask(createTaskRequest);
        assertThrows(ToDoListException.class,()->appService.deleteTask(deleteTaskRequest));
        deleteTaskRequest.setUsername("username");
        deleteTaskRequest.setPassword("password");
        deleteTaskRequest.setTaskName("task1");
        appService.deleteTask(deleteTaskRequest);
        assertEquals(0, appService.countAllUserTask("username"));
    }
    @Test
    public void updateTask_TestTaskIsUpdated(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword("password");
        UpdateTaskRequest updateTask = new UpdateTaskRequest();
        updateTask.setNewTitle("task2");
        updateTask.setDescription("pillow");
        assertThrows(ToDoListException.class,()->appService.updateTask(updateTask));
        updateTask.setOldTitle("task1");
        assertThrows(ToDoListException.class,()->appService.updateTask(updateTask));
        updateTask.setPassword("password");
        assertThrows(ToDoListException.class,()->appService.updateTask(updateTask));
        appService.register(request);
        assertThrows(ToDoListException.class,()->appService.register(request));
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setUsername("username");
        createTaskRequest.setDueDate("2024-05-31");
        createTaskRequest.setTaskTitle("task1");
        createTaskRequest.setDescription("decription");
        CreateTaskResponse  createResponse = appService.createTask(createTaskRequest);
        updateTask.setUsername("username");
        updateTask.setPassword("password");
        UpdateTaskResponse updateResponse = appService.updateTask(updateTask);
        assertEquals("task2",updateResponse.getTaskTitle());
        assertEquals(TaskStatus.PENDING,updateResponse.getStatus());
        assertEquals("2024-05-31",updateResponse.getDueDate());
        assertEquals("username",updateResponse.getUsername());

    }
    @Test
    public void startTask_testTaskIsStarted(){
        StartTaskRequest startTaskRequest = new StartTaskRequest();
        startTaskRequest.setUsername("usernam");
        startTaskRequest.setTaskName("task1");
        assertThrows(ToDoListException.class,()->appService.startTask(startTaskRequest));
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password");
        appService.register(register);
        assertThrows(ToDoListException.class,()->appService.startTask(startTaskRequest));
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setUsername("username");
        createTaskRequest.setTaskTitle("task1");
        createTaskRequest.setDueDate("2024-07-12");
        createTaskRequest.setDescription("timing");
        appService.createTask(createTaskRequest);
        assertThrows(ToDoListException.class,()->appService.startTask(startTaskRequest));
        startTaskRequest.setUsername("username");
        startTaskRequest.setPassword("password");
        StartTaskResponse startTaskResponse = appService.startTask(startTaskRequest);
        assertEquals("username", startTaskResponse.getUsername());
        assertEquals(TaskStatus.IN_PROGRESS, startTaskResponse.getStatus());
    }
    @Test
    public void completeTask_testTaskIsCompleted(){
        CompleteTaskRequest completeTaskRequest = new CompleteTaskRequest();
        assertThrows(ToDoListException.class,()->appService.completeTask(completeTaskRequest));
        StartTaskRequest startTaskRequest = new StartTaskRequest();
        startTaskRequest.setUsername("usernam");
        startTaskRequest.setTaskName("task1");
        assertThrows(ToDoListException.class,()->appService.startTask(startTaskRequest));
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password");
        appService.register(register);
        assertThrows(ToDoListException.class,()->appService.startTask(startTaskRequest));
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setUsername("username");
        createTaskRequest.setTaskTitle("task1");
        createTaskRequest.setDueDate("2024-07-12");
        createTaskRequest.setDescription("timing");
        appService.createTask(createTaskRequest);
        assertThrows(ToDoListException.class,()->appService.startTask(startTaskRequest));
        startTaskRequest.setUsername("username");
        startTaskRequest.setPassword("password");
        assertThrows(ToDoListException.class,()->appService.completeTask(completeTaskRequest));
        completeTaskRequest.setPassword("password");
        StartTaskResponse startTaskResponse = appService.startTask(startTaskRequest);
        assertEquals("username", startTaskResponse.getUsername());
        assertEquals(TaskStatus.IN_PROGRESS, startTaskResponse.getStatus());
    }
    @Test
    public void assignTask_TestTaskIsAssigned(){
        RegisterRequest register = new RegisterRequest();
        register.setUsername("username");
        register.setPassword("password");
        appService.register(register);

        assertThrows(ToDoListException.class,()->appService.register(register));

        AssignTaskRequest assignTaskRequest = new AssignTaskRequest();
        assignTaskRequest.setTaskTitle("task1");
        assignTaskRequest.setAssignerUsername("username");
        assignTaskRequest.setAssigneeUsername("user10");
        assignTaskRequest.setDueDate("2024-06-10");
        assignTaskRequest.setDescription("description for creating task");
        assignTaskRequest.setPassword("password");
        assertThrows(ToDoListException.class,()->appService.assignTask(assignTaskRequest));

        register.setUsername("user10");
        assertEquals(1,appService.countAllUsers());
        register.setPassword("password");
        appService.register(register);

        assertEquals(2,appService.countAllUsers());

        assertEquals(0,appService.countAllUserTask("user10"));

        assertEquals(0,appService.countAllUserTask("username"));
        appService.assignTask(assignTaskRequest);
        assertThrows(ToDoListException.class,()->appService.assignTask(assignTaskRequest));
        assertEquals(1,appService.countAllUserTask("username"));
        assertEquals(1,appService.countAllUserTask("user10"));
        LoginRequest login = new LoginRequest();
        LoginRequest login1 = new LoginRequest();
        login.setUsername("user10");
        login1.setUsername("username");
        login1.setPassword("password");
        login.setPassword("password1");
        assertThrows(ToDoListException.class,()->appService.login(login));
        login.setPassword("password");
        LoginResponse loginResponse = appService.login(login);
        assertEquals(0, appService.login(login1).getNotification().size());
        assertEquals(1,loginResponse.getNotification().size());
    }
    @Test
    public void AssignedTaskCanBeMonitoredTaskAssigned(){
        RegisterRequest register = new RegisterRequest();
        register.setUsername("user1");
        register.setPassword("pass");
        appService.register(register);
        register.setUsername("user2");
        register.setPassword("pass");
        appService.register(register);
        AssignTaskRequest assignTaskRequest = new AssignTaskRequest();
        assignTaskRequest.setTaskTitle("task1");
        assignTaskRequest.setAssignerUsername("user1");
        assignTaskRequest.setAssigneeUsername("user2");
        assignTaskRequest.setDueDate("2024-06-10");
        assignTaskRequest.setDescription("description for creating task");
        assignTaskRequest.setPassword("password");
        assertThrows(ToDoListException.class,()->appService.assignTask(assignTaskRequest));
        assignTaskRequest.setPassword("pass");
        appService.assignTask(assignTaskRequest);
        LoginRequest login = new LoginRequest();
        login.setUsername("user1");
        login.setPassword("pass");
        LoginRequest login1 = new LoginRequest();
        login1.setUsername("user2");
        login1.setPassword("pass");
        LoginResponse loginResponse = appService.login(login);
        assertEquals(1, appService.login(login1).getNotification().size());
        assertEquals(0,loginResponse.getNotification().size());
        StartTaskRequest start = new StartTaskRequest();
        start.setUsername("user1");
        start.setPassword("pas1s");
        start.setTaskName("task1");
        assertThrows(ToDoListException.class,()->appService.startTask(start));
        start.setUsername("user2");
        assertThrows(ToDoListException.class,()->appService.startTask(start));
        start.setPassword("pass");
        appService.startTask(start);
        LogOut logout= new LogOut();
        logout.setUsername("user2");
        logout.setPassword("password");
        assertThrows(ToDoListException.class,()->appService.logOut(logout));
        logout.setPassword("pass");
        assertEquals(0, appService.login(login1).getNotification().size());
        CompleteTaskRequest complete = new CompleteTaskRequest();
        complete.setUsername("user2");
        complete.setTaskName("task12");
        complete.setPassword("pass1");
        assertThrows(ToDoListException.class,()->appService.completeTask(complete));
        complete.setPassword("pass");
        assertThrows(ToDoListException.class,()->appService.completeTask(complete));
        complete.setTaskName("task1");
        appService.completeTask(complete);
        assertEquals(0,appService.login(login1).getNotification().size());
        List<Notifier> notes = appService.login(login).getNotification();
        assertEquals(2,notes.size());
    }
    @Test
    public void testFindAllTask(){
        RegisterRequest register= new RegisterRequest();
        register.setUsername("user");
        register.setPassword("pass");
        appService.register(register);
        LoginRequest login = new LoginRequest();
        login.setUsername("user");
        login.setPassword("pass");
        List<ViewTaskResponse> response = appService.findAllTask(login);
        assertEquals(0, response.size());
        CreateTaskRequest create = new CreateTaskRequest();
        create.setUsername("user");
        create.setTaskTitle("task1");
        create.setDueDate("2024-05-15");
        appService.createTask(create);
        create.setTaskTitle("title");
        appService.createTask(create);
        create.setTaskTitle("title1");
        appService.createTask(create);
        assertEquals(3,appService.findAllTask(login).size());
        StartTaskRequest start = new StartTaskRequest();
        start.setUsername("user");
        start.setTaskName("title");
        start.setPassword("pass");
        appService.startTask(start);
        CompleteTaskRequest  complete = new CompleteTaskRequest();
        complete.setTaskName("title");
        complete.setPassword("pass");
        complete.setUsername("user");
        appService.completeTask(complete);
        start.setTaskName("title1");
        assertEquals(1,appService.getAllCompleteTask(login).size());
        assertEquals(2,appService.getAllPendingTask(login).size());
        assertEquals(2, appService.getAllTaskNotCompleted(login).size());
        assertEquals(0,appService.getAllAssignedTask(login).size());
    }
    @Test
    public void testNotificationsFromOverDueDueTasksAreGotten(){
        RegisterRequest register= new RegisterRequest();
        register.setUsername("user");
        register.setPassword("pass");
        appService.register(register);

        LoginRequest login = new LoginRequest();
        login.setUsername("user");
        login.setPassword("pass");
        CreateTaskRequest create = new CreateTaskRequest();
        create.setUsername("user");
        create.setTaskTitle("task1");
        create.setDueDate("2024-05-16");
        appService.createTask(create);
        LoginResponse notifications = appService.login(login);

        assertEquals(0, notifications.getNotification().size());
        register.setUsername("user2");
        register.setPassword("pass");
        appService.register(register);

        AssignTaskRequest assignTaskRequest = new AssignTaskRequest();
        assignTaskRequest.setTaskTitle("task1");
        assignTaskRequest.setAssignerUsername("user");
        assignTaskRequest.setAssigneeUsername("user2");
        assignTaskRequest.setDueDate("2024-06-10");
        assignTaskRequest.setDescription("description for creating task");
        assignTaskRequest.setPassword("password");
        assertThrows(ToDoListException.class,()->appService.assignTask(assignTaskRequest));
        assignTaskRequest.setPassword("pass");
        appService.assignTask(assignTaskRequest);
        StartTaskRequest start = new StartTaskRequest();
        start.setTaskName("task1");
        start.setUsername("user2");
        start.setPassword("pass");
        appService.startTask(start);
        notifications = appService.login(login);
        assertEquals(1, notifications.getNotification().size());
        notifications.getNotification().forEach(System.out::println);
        CompleteTaskRequest complete = new CompleteTaskRequest();
        complete.setUsername("user2");
        complete.setTaskName("task1");
        complete.setPassword("pass");
        appService.completeTask(complete);
        LoginResponse response = appService.login(login);
        assertEquals(1, response.getNotification().size());
    }
}


