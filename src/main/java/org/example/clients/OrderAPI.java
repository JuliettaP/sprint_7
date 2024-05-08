package org.example.clients;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.example.objects.*;

import static io.restassured.RestAssured.given;

public class OrderAPI {

    public static final String ORDERS = "/api/v1/orders";

    public ValidatableResponse createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    public void deleteOrder(int track) {
       // в документации не нашла как удалить заказ
    }
}

