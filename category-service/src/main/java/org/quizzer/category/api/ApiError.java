package org.quizzer.category.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Data
public class ApiError {
    @JsonSerialize(using = HttpStatusSerializer.class, as = HttpStatus.class)
    private final HttpStatus status;

    private final String message;

    @Builder.Default
    private final ZonedDateTime timestamp = ZonedDateTime.now();

    @Singular
    private final List<ApiFieldError> fieldErrors;

    @JsonIgnore
    @Builder.Default
    private final HttpHeaders headers = new HttpHeaders();

    public ResponseEntity<Object> toResponseEntity() {
        return new ResponseEntity<>(this, headers, status);
    }

    private static class HttpStatusSerializer extends JsonSerializer<HttpStatus> {
        @Override
        public void serialize(HttpStatus status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(status.value());
        }
    }
}
