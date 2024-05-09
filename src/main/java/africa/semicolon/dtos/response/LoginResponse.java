package africa.semicolon.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private List<Notifier> notification;
}
