package org.codiz.onshop.service.serv;

import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;

public interface UsersService {

    EntityCreationResponse registerUser(UserRegistrationRequest request);
}
