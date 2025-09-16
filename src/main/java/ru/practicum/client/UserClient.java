package ru.practicum.client;

import ru.practicum.data.CreateUserRequest;
import ru.practicum.data.LoginUserRequest;
import ru.practicum.data.UpdateUserRequest;
import io.restassured.response.Response;
import ru.practicum.defaults.Endpoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserClient extends RestClient {
    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    public Response createUser(CreateUserRequest createUserRequest) {
        log.info("Creating user with email: {}", createUserRequest.getEmail());
        return getDefaultRequestSpecification()
                .body(createUserRequest)
                .when()
                .post(Endpoints.AUTH_REGISTER);
    }

    public Response loginUser(LoginUserRequest loginUserRequest) {
        log.info("Logging in user with email: {}", loginUserRequest.getEmail());
        return getDefaultRequestSpecification()
                .body(loginUserRequest)
                .when()
                .post(Endpoints.AUTH_LOGIN);
    }

    public Response deleteUser(String bearerToken) {
        log.info("Deleting user with token");
        return getDefaultRequestSpecification()
                .header("authorization", bearerToken)
                .when()
                .delete(Endpoints.AUTH_USER);
    }

    public Response updateUser(String bearerToken, UpdateUserRequest updateUserRequest) {
        log.info("Updating user with token");
        return getDefaultRequestSpecification()
                .header("authorization", bearerToken)
                .body(updateUserRequest)
                .when()
                .patch(Endpoints.AUTH_USER);
    }

    public Response updateUserWithoutAuth(UpdateUserRequest updateUserRequest) {
        log.info("Updating user without authentication");
        return getDefaultRequestSpecification()
                .body(updateUserRequest)
                .when()
                .patch(Endpoints.AUTH_USER);
    }
}