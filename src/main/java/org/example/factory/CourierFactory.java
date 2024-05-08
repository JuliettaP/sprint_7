package org.example.factory;

import com.github.javafaker.Faker;
import org.example.objects.Courier;

public class CourierFactory {
    static Faker faker = new Faker();

    public static Courier newCourier() {
        var courier = new Courier();
        courier.setLogin(faker.name().username());
        courier.setPassword(faker.internet().password());
        courier.setFirstName(faker.name().firstName());
        return courier;
    }

    public static Courier newCourierEmptyPassword() {
        var courier = new Courier();
        courier.setLogin(faker.name().username());
        courier.setPassword("");
        courier.setFirstName(faker.name().firstName());
        return courier;
    }

    public static Courier newCourierNoPassword() {
        var courier = new Courier();
        courier.setLogin( faker.name().username());
        courier.setFirstName(faker.name().firstName());
        return courier;
    }
}
