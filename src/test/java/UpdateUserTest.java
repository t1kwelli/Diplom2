import api.client.UserClient;
import api.model.User;
import api.util.*;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UpdateUserTest {

    private static final User USER_UPDATE = UserGenerator.getUniqueUser();

    private User user;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp () throws InterruptedException {
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
        ValidatableResponse createResponse = userClient.create(user);
        String accessToken = createResponse.extract().path("accessToken");
        token = accessToken.substring(7);
    }

    @After
    public void cleanUp () throws InterruptedException {
        if (token != null) userClient.delete(token);
    }

    @Test
    @Description("Проверка изменения поля \"name\" c авторизацией")
    public void updateNameWithAuthorizationTest () throws InterruptedException {
        userClient.login(UserAuthorizationData.from(user));
        ValidatableResponse updateResponse = userClient.updateNameWithAuthorization(token, UserName.from(USER_UPDATE));

        int actualStatusCode = updateResponse.extract().statusCode();
        boolean actualResult = updateResponse.extract().path("success");

        assertEquals(200, actualStatusCode);
        assertEquals(true, actualResult);
    }

    @Test
    @Description("Проверка изменения поля \"name\" без авторизации")
    public void updateNameWithoutAuthorizationTest () throws InterruptedException {
        ValidatableResponse updateResponse = userClient.updateNameWithoutAuthorization(UserName.from(USER_UPDATE));

        int actualStatusCode = updateResponse.extract().statusCode();
        String actualMessage = updateResponse.extract().path("message");

        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", actualMessage);
    }

    @Test
    @Description("Проверка изменения поля \"password\" с авторизацией")
    public void updatePasswordWithAuthorizationTest () throws InterruptedException {
        userClient.login(UserAuthorizationData.from(user));
        ValidatableResponse updateResponse = userClient.updatePasswordWithAuthorization(token, UserPassword.from(USER_UPDATE));

        int actualStatusCode = updateResponse.extract().statusCode();
        boolean actualResult = updateResponse.extract().path("success");

        assertEquals(200, actualStatusCode);
        assertEquals(true, actualResult);
    }

    @Test
    @Description("Проверка изменения поля \"password\" без авторизации")
    public void updatePasswordWithoutAuthorizationTest () throws InterruptedException {
        ValidatableResponse updateResponse = userClient.updatePasswordWithoutAuthorization(UserPassword.from(USER_UPDATE));

        int actualStatusCode = updateResponse.extract().statusCode();
        String actualMessage = updateResponse.extract().path("message");

        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", actualMessage);
    }

    @Test
    @Description("Проверка изменения поля \"email\" с авторизацией")
    public void updateEmailWithAuthorizationTest () throws InterruptedException {
        userClient.login(UserAuthorizationData.from(user));
        ValidatableResponse updateResponse = userClient.updateEmailWithAuthorization(token, UserEmail.from(USER_UPDATE));

        int actualStatusCode = updateResponse.extract().statusCode();
        boolean actualResult = updateResponse.extract().path("success");

        assertEquals(200, actualStatusCode);
        assertEquals(true, actualResult);
    }

    @Test
    @Description("Проверка изменения поля \"email\" без авторизации")
    public void updateEmailWithoutAuthorizationTest () throws InterruptedException {
        ValidatableResponse updateResponse = userClient.updateEmailWithoutAuthorization(UserEmail.from(USER_UPDATE));

        int actualStatusCode = updateResponse.extract().statusCode();
        String actualMessage = updateResponse.extract().path("message");

        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", actualMessage);
    }
}
