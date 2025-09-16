package ru.practicum.client;

import ru.practicum.data.CreateOrderRequest;
import io.restassured.response.Response;

public class OrderClient extends RestClient {

    public Response getIngridients() {
        return getDefaultRequestSpecification()
                .when()
                .get("/ingredients");
    }

    public Response createOrderWithAuth(CreateOrderRequest createOrderRequest, String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(createOrderRequest)
                .when()
                .post("/orders");
    }

    public Response createOrderWithoutAuth(CreateOrderRequest createOrderRequest) {
        return getDefaultRequestSpecification()
                .body(createOrderRequest)
                .when()
                .post("/orders");
    }

    public Response getUserOrdersWithoutAuth() {
        return getDefaultRequestSpecification()
                .when()
                .get("/orders");
    }

    public Response getUserOrdersWithAuth(String accessToken) {
        return getDefaultRequestSpecification()
                .header("authorization", accessToken)
                .when()
                .get("/orders");
    }

    public Response getAllOrders() {
        return getDefaultRequestSpecification()
                .when()
                .get("/orders/all");
    }
}