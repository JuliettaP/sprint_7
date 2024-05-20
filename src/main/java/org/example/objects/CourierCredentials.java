package org.example.objects;

public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredentials build(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }
}
