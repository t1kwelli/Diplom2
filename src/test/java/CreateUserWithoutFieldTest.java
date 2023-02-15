import api.client.UserClient;
import api.model.User;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateUserWithoutFieldTest {

    private User user;
    private UserClient userClient;

    private final String name;
    private final String password;
    private final String email;
    private final int expectedStatusCode;
    private final String expectedMessage;

    public CreateUserWithoutFieldTest (String name, String password, String email, int expectedStatusCode, String expectedMessage) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
    }

    @Before
    public void setUp () {
        userClient = new UserClient();
        user = new User(name, password, email);
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}, {1}, {2}, {3}, {4}")
    public static Object[][] getUserData () {
        return new Object[][]{
                {null, "123", "TestKD@gmail.com", 403, "Email, password and name are required fields"},
                {"Test KD", null, "TestKD@gmail.com", 403, "Email, password and name are required fields"},
                {"Test KD", "123", null, 403, "Email, password and name are required fields"},
        };
    }

    @Test
    @Description("Проверка создания пользователя, если не заполнено одно из обязательных полей")
    public void createUserWithoutRequiredField () throws InterruptedException {
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(expectedStatusCode, statusCode);
        assertEquals(expectedMessage, message);
    }
}
