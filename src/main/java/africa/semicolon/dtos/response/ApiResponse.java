package africa.semicolon.dtos.response;

import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private boolean isSuccessful;
    private Object data;
}
