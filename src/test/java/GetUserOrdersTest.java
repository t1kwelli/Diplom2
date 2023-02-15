import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import api.model.UserOrders;
import api.util.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class GetUserOrdersTest {

    private Order order;
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String token;
    private final List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp () throws InterruptedException {
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();

        orderClient = new OrderClient();
    }

    @After
    public void cleanUp () throws InterruptedException {
        if (token != null) userClient.delete(token);
    }

    @Test
    @Description("Проверка получения заказов конкретного пользователя с авторизацией")
    public void getUserOrdersWithAuthorizationTest () throws InterruptedException {
        ValidatableResponse createUserResponse = userClient.create(user);
        String accessToken = createUserResponse.extract().path("accessToken");
        token = accessToken.substring(7);

        ValidatableResponse ingredientsResponse = orderClient.getDataIngredients();
        String idIngredient = ingredientsResponse.extract().path("data[0]._id");
        ingredients.add(idIngredient);
        order = new Order(ingredients);

        orderClient.createOrderWithToken(order, token);

        ValidatableResponse response = orderClient.getUserOrders(token);

        UserOrders userOrders = response.extract().body().as(UserOrders.class);
        int statusCode = response.extract().statusCode();

        assertEquals(200, statusCode);
        assertThat(userOrders.getOrders(), notNullValue());
        assertEquals(true, userOrders.isSuccess());
        assertThat(userOrders.getTotal(), notNullValue());
        assertThat(userOrders.getTotalToday(), notNullValue());
    }

    @Test
    @Description("Проверка получения заказов конкретного пользователя без авторизации")
    public void getUserOrdersWithoutAuthorizationTest () throws InterruptedException {
        ValidatableResponse response = orderClient.getUserOrdersWithoutAuthorization();

        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");

        assertEquals(401, statusCode);
        assertEquals("You should be authorised", message);
    }
}
