import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.clients.*;
import org.example.objects.*;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.example.clients.CourierAPI.BASE_URI;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    static Faker faker = new Faker();
    private final List<String> color;
    private int trackId;
    OrderAPI orderApi = new OrderAPI();

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "{index}: Заказ с цветом: {0}")
    public static Object[][] getParameters() {
        return new Object[][]{
                {List.of("GREY")}, // можно указать один из цветов — GREY
                {List.of("BLACK")}, // можно указать один из цветов — BLACK
                {List.of("GREY", "BLACK")}, // можно указать оба цвета
                {List.of()}, // можно совсем не указывать цвет
        };
    }

    @Before
    public void init() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Создание заказа с выбором цвета: ручка /api/v1/orders")
    @Description("Проверка, что можно создать заказ на: серый цвет, черный цвет, выбор двух цветов, без выбора цвета")
    public void runTest() {
        Order order = new Order(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                faker.number().numberBetween(1, 100),
                faker.phoneNumber().cellPhone(),
                faker.number().numberBetween(1, 7),
                "2024-06-06",
                faker.chuckNorris().fact(),
                color);
        ValidatableResponse response = orderApi.createOrder(order);
        trackId = response.extract().path("track");
        response.assertThat().body("$", Matchers.allOf(Matchers.hasKey("track")))
                .statusCode(HttpStatus.SC_CREATED);

    }

    @After
    public void cleanup() {
        orderApi.deleteOrder(trackId);
    }
}
