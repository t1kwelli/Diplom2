import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import api.util.UserGenerator;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String token;
    private List <String> ingredients;

    @Before
    public void setUp () throws InterruptedException {
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
        ValidatableResponse createUserResponse = userClient.create(user);
        String accessToken = createUserResponse.extract().path("accessToken");
        token = accessToken.substring(7);

        orderClient = new OrderClient();
        ingredients = new ArrayList<>();
    }

    @After
    public void cleanUp () throws InterruptedException {
        if (token != null) userClient.delete(token);
    }

    @Test
    @Description("Проверка создания заказа с авторизацией и с ингредиентами")
    public void createOrderTest () throws InterruptedException {
        ValidatableResponse ingredientsResponse = orderClient.getDataIngredients();
        String idIngredient = ingredientsResponse.extract().path("data[0]._id");
        ingredients.add(idIngredient);
        order = new Order(ingredients);

        ValidatableResponse createOrderResponse = orderClient.createOrderWithToken(order, token);

        int statusCode = createOrderResponse.extract().statusCode();
        boolean result = createOrderResponse.extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(result);
    }

    @Test
    @Description("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuthorizationTest () throws InterruptedException {
        ValidatableResponse ingredientsResponse = orderClient.getDataIngredients();
        String idIngredient = ingredientsResponse.extract().path("data[0]._id");
        ingredients.add(idIngredient);
        order = new Order(ingredients);

        ValidatableResponse createOrderResponse = orderClient.createOrderWithoutToken(order);

        int statusCode = createOrderResponse.extract().statusCode();
        boolean result = createOrderResponse.extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(result);
    }

    @Test
    @Description("Проверка создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest () throws InterruptedException {
        order = new Order(ingredients);

        ValidatableResponse createOrderResponse = orderClient.createOrderWithToken(order, token);

        int statusCode = createOrderResponse.extract().statusCode();
        String message = createOrderResponse.extract().path("message");

        assertEquals(400, statusCode);
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    @Description("Проверка создания заказа c неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientTest () throws InterruptedException {
        ingredients.add("incorrectIngredient");
        order = new Order(ingredients);

        ValidatableResponse createOrderResponse = orderClient.createOrderWithToken(order, token);

        int statusCode = createOrderResponse.extract().statusCode();

        assertEquals(500, statusCode);
    }
}
