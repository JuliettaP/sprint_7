import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.example.clients.CourierAPI.BASE_URI;


public class OrderListTest {
    public static final String ORDERS = "/api/v1/orders";

    @Before
    public void init() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Получение списка заказов: ручка /api/v1/orders")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void getListOrdersTest() {
        Response response =
                given()
                .get(ORDERS);

        response.then().assertThat().body("$", Matchers.allOf(
                        Matchers.hasKey("orders"),
                        Matchers.hasKey("pageInfo"),
                        Matchers.hasKey("availableStations")))
                .statusCode(HttpStatus.SC_OK);
    }
}
