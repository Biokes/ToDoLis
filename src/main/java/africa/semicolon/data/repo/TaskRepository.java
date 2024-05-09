package africa.semicolon.data.repo;

import africa.semicolon.data.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String>{
    Optional<Task> findTaskByTaskTitleIgnoreCaseAndUsernameIgnoreCase(String title, String username);
    void deleteTaskByUsernameAndTaskTitle(String username, String taskName);

    List<Task> findByUsernameIgnoreCase(String username);
}
