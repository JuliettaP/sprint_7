package org.example.clients;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.example.objects.*;

import static io.restassured.RestAssured.given;

public class CourierAPI {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private static final String CREATE = "api/v1/courier";
    private static final String LOGIN = "api/v1/courier/login";

    private static final String DELETE = "/api/v1/courier/";

    public CourierAPI() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(courier)
                .when()
                .post(CREATE)
                .then();
    }

    @Step("Логин курьера в системе")
    public ValidatableResponse loginCourier(CourierCredentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(credentials)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Удаление курьера по id")
    public void deleteCourier(int courierId) {
        given()
                .contentType(ContentType.JSON)
                .delete(DELETE + courierId)
                .then();
    }
}
