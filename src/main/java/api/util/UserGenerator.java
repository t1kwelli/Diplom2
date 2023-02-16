package api.util;

import api.model.User;
import com.github.javafaker.Faker;

public class UserGenerator {

    private static final Faker faker = new Faker();

    private static final String NAME = faker.name().fullName();
    private static final String PASSWORD = faker.internet().password();
    private static final String EMAIL = faker.internet().emailAddress();

    public static User getUniqueUser() { return new User(NAME, PASSWORD, EMAIL); }

}
