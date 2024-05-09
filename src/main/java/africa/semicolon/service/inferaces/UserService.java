package africa.semicolon.service.inferaces;

import africa.semicolon.data.model.User;
import africa.semicolon.dtos.request.DeleteUserRequest;
import africa.semicolon.dtos.request.LogOut;
import africa.semicolon.dtos.request.LoginRequest;
import africa.semicolon.dtos.request.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void save(User user);
    void register(RegisterRequest request);
    long countAllUsers();
    void deleteUser(DeleteUserRequest delete);
    void deleteAll();
    void login(LoginRequest login);
    void isValidUsername(String username);
    User getUser(LoginRequest login);
    User getUser(String username);
    void logOut(LogOut logout);
    void validateUserLogin(LoginRequest login);
}
