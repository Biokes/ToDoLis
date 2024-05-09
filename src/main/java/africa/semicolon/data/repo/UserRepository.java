package africa.semicolon.data.repo;

import africa.semicolon.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findAllByUsernameIgnoreCase(String username);
}
