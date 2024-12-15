package org.codiz.onshop.dtos.requests;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class CancelOrderRequest {

    String orderItemId;
    String userId;
}
