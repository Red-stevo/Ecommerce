package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
public class EntityDeletionResponse {
    private boolean success;
    private String message;
    private Timestamp timestamp;
    private HttpStatus status;
}
