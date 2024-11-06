package org.codiz.onshop.service.serv.users;

import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.springframework.stereotype.Service;

@Service
public interface UsersService {

    EntityCreationResponse registerUser(UserRegistrationRequest request);
}
