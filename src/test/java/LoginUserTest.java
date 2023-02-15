import api.client.UserClient;
import api.model.User;
import api.util.UserAuthorizationData;
import api.util.UserGenerator;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {

    private User user;
    private UserClient userClient;
    private String token;

    private static final Faker faker = new Faker();

    @Before
    public void setUp() throws InterruptedException {
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
        ValidatableResponse response = userClient.create(user);
        String accessToken = response.extract().path("accessToken");
        token = accessToken.substring(7);
    }

    @After
    public void cleanUp() throws InterruptedException {
        if (token != null) userClient.delete(token);
    }

    @Test
    @Description("Проверка логина под существующим пользователем")
    public void loginExistingUserTest () throws InterruptedException {
        ValidatableResponse response = userClient.login(UserAuthorizationData.from(user));

        int statusCode = response.extract().statusCode();
        boolean success = response.extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(success);
    }

    @Test
    @Description("Проверка логина под неверным email")
    public void loginWithIncorrectEmailTest () throws InterruptedException {
        String email = faker.internet().emailAddress();
        String password = user.getPassword();
        String name = user.getName();
        user = new User(name, password, email);

        ValidatableResponse response = userClient.login(UserAuthorizationData.from(user));

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(401, statusCode);
        assertEquals("email or password are incorrect", message);
    }

    @Test
    @Description("Проверка логина под неверным паролем")
    public void loginWithIncorrectPasswordTest () throws InterruptedException {
        String email = user.getEmail();
        String password = faker.internet().password();
        String name = user.getName();
        user = new User(name, password, email);

        ValidatableResponse response = userClient.login(UserAuthorizationData.from(user));

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(401, statusCode);
        assertEquals("email or password are incorrect", message);
    }
}
