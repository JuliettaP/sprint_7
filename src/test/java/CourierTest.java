import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.objects.*;
import org.example.clients.*;
import org.example.factory.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierTest {
    private final CourierAPI courierApi = new CourierAPI();
    private Courier courier;
    private int courierId;

    @Before
    public void init()
    {
        courier = CourierFactory.newCourier();
    }

    @Test
    @DisplayName("Штатное создание курьера: ручка api/v1/courier")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testCreateCourier() {
        ValidatableResponse createResponse = courierApi.createCourier(courier);
        createResponse.assertThat().statusCode(HttpStatus.SC_CREATED).body("ok", is(true));
        // для того чтобы иметь возможность удалить созданного в ходе тесте курьера,
        // нам необходимо сделать login и получить в ответ id, по которому мы уже сможем удалить курьера
        ValidatableResponse loginResponse = courierApi.loginCourier(CourierCredentials.build(courier));
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Вход курьера в систему: ручка api/v1/courier/login")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testLoginCourier() {
        courierApi.createCourier(courier);
        // здесь сам тест направлен на проверку входа курьера в систему, поэтому мы получим id в ответ на
        // соотв. запрос и сможем удалить созданного тестового курьера в @After теста
        ValidatableResponse response = courierApi.loginCourier(CourierCredentials.build(courier));
        courierId = response.extract().path("id");
        response.assertThat().statusCode(HttpStatus.SC_OK).body("id", notNullValue());
    }

    @Test
    @DisplayName("Создание курьера с повторяющимся логином: ручка api/v1/courier")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testCreateCourierSameLogin() {
        courierApi.createCourier(courier);
        // здесь курьер не создается, поэтому удалять нечего
        ValidatableResponse response = courierApi.createCourier(courier);
        response.assertThat().statusCode(HttpStatus.SC_CONFLICT).body("message",
                is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера с пустым паролем: ручка api/v1/courier")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testCreateCourierEmptyPassword() {
        // здесь курьер не создается, поэтому удалять нечего
        ValidatableResponse response = courierApi.createCourier(CourierFactory.newCourierEmptyPassword());
        response.assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).body("message",
                is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без поля пароля: ручка api/v1/courier")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testCreateCourierNoPassword() {
        // здесь курьер не создается, поэтому удалять нечего
        ValidatableResponse response = courierApi.createCourier(CourierFactory.newCourierNoPassword());
        response.assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).body("message",
                is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Вход курьера с несущестующей парой логин/пароль в систему: ручка api/v1/courier/login")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testLoginCourierBadCredentials() {
        // здесь курьер не создается, поэтому удалять нечего
        ValidatableResponse response = courierApi.loginCourier(CourierCredentials.build(CourierFactory.newCourier()));
        response.assertThat().statusCode(HttpStatus.SC_NOT_FOUND).body("message",
                is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Вход курьера без пароля в систему: ручка api/v1/courier/login")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void testLoginCourierEmptyPassword() {
        // здесь курьер не создается, поэтому удалять нечего
        ValidatableResponse response = courierApi.loginCourier(CourierCredentials.build(CourierFactory.newCourierEmptyPassword()));
        response.assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).body("message",
                is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вход курьера без поля пароля в систему: ручка api/v1/courier/login")
    @Description("Проверка ожидаемого результата: statusCode")
    public void testLoginCourierNoPassword() {
        // здесь курьер не создается, поэтому удалять нечего
        ValidatableResponse response = courierApi.loginCourier(CourierCredentials.build(CourierFactory.newCourierNoPassword()));
        response.assertThat().statusCode(HttpStatus.SC_GATEWAY_TIMEOUT);
    }

    @After
    public void cleanup()
    {
        if (courierId != 0)
            courierApi.deleteCourier(courierId);
    }
}
