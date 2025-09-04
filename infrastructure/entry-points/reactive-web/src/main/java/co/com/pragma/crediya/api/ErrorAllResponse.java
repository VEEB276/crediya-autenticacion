package co.com.pragma.crediya.api;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorAllResponse {
    private final String code;
    private final String message;
    private final int status;
    private final String path;
    private final Instant timestamp;
    private final String correlationId;
    private final List<ErrorDetail> details;

    public ErrorAllResponse(String code, String message, int status, String path, Instant timestamp, String correlationId, List<ErrorDetail> details) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.details = details;
    }

    public static ErrorAllResponse of(String code, String message, int status, String path, List<ErrorDetail> details, String correlationId) {
        return new ErrorAllResponse(code, message, status, path, Instant.now(), correlationId, details);
    }
}
