package org.codiz.onshop.dtos.response;

import lombok.Data;
import lombok.Setter;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Setter
public class EntityCreationResponse {

    private String message;
    private Timestamp createdAt;
    private HttpStatus status;

}
