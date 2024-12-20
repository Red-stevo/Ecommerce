package org.codiz.onshop.dtos.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class UserGeneralResponse {

    private String message;

    private Date date;

    private HttpStatus httpStatus;
}
