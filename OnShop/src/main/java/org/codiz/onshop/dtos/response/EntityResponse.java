package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
public class EntityResponse {

    private String message;
    private Timestamp createdAt;
    private HttpStatus status;

}
