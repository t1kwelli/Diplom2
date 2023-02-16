package api.client;

import api.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;


public class OrderClient extends Client{
    private static final String PATH_CREATE_ORDER = "api/orders";

    private static final String PATH_GET_INGREDIENTS = "api/ingredients";

    private static final String PATH_GET_USER_ORDERS = "api/orders";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithToken (Order order, String token) throws InterruptedException {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .and()
                .body(order)
                .when()
                .post(PATH_CREATE_ORDER)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutToken (Order order) throws InterruptedException {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(PATH_CREATE_ORDER)
                .then();
    }

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getDataIngredients () throws InterruptedException {
        return given()
                .spec(getSpec())
                .get(PATH_GET_INGREDIENTS)
                .then();
    }

    @Step("Получение заказов конкретного пользователя c авторизацией")
    public ValidatableResponse getUserOrders (String token) throws InterruptedException {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .get(PATH_GET_USER_ORDERS)
                .then();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse getUserOrdersWithoutAuthorization () throws InterruptedException {
        return given()
                .spec(getSpec())
                .get(PATH_GET_USER_ORDERS)
                .then();
    }
}
