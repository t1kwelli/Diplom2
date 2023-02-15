import api.client.UserClient;
import api.model.User;
import api.util.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {

    private User user;

    private UserClient userClient;

    private String token;

    @Before
    public void setUp () {
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
    }

    @After
    public void cleanUp() throws InterruptedException {
        if (token != null) userClient.delete(token);
    }

    @Test
    @Description("Проверка создания уникального пользователя")
    public void createUniqueUserTest () throws InterruptedException {
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        String accessToken = response.extract().path("accessToken");

        assertEquals(200, statusCode);
        assertThat(accessToken, notNullValue());

        token = accessToken.substring(7);
    }

    @Test
    @Description("Проверка создания пользователя, который уже зарегистрирован")
    public void createExistingUserTest () throws InterruptedException {
        // создаём уникального пользователя
        ValidatableResponse successResponse = userClient.create(user);

        // подготавливаем такие же данные для нового пользователя
        String name = user.getName();
        String password = user.getPassword();
        String email = user.getEmail();
        user = new User(name, password, email);

        ValidatableResponse failResponse = userClient.create(user);

        String accessToken = successResponse.extract().path("accessToken");
        token = accessToken.substring(7);

        int statusCode = failResponse.extract().statusCode();
        String message = failResponse.extract().path("message");

        assertEquals(403, statusCode);
        assertEquals("User already exists", message);
    }
}
