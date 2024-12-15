package org.codiz.onshop.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class OrderPlacementRequest {
    private String userId;
    private String officeAddress;
    private DoorStepAddressCoordinates doorStepAddress;

}
