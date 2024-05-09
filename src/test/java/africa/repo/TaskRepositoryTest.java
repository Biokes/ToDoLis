package africa.repo;

import africa.semicolon.data.model.Task;
import africa.semicolon.data.repo.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class TaskRepositoryTest{
    @Autowired
    private TaskRepository repo;
    @BeforeEach
    public void deleteAll(){
    repo.deleteAll();
    }
    @Test
    public void createTask_testTaskIsCreated(){
        Task task = new Task();
        repo.save(task);
        assertEquals(1,repo.count());
    }
}