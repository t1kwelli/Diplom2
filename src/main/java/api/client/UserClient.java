package api.client;

import api.model.User;
import api.util.UserAuthorizationData;
import api.util.UserEmail;
import api.util.UserName;
import api.util.UserPassword;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends Client{

    private static final String PATH_CREATE_USER = "api/auth/register";
    private static final String PATH_LOGIN_USER = "api/auth/login";
    private static final String PATH_UPDATE_USER = "api/auth/user";
    private static final String PATH_DELETE_USER = "api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) throws InterruptedException {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(PATH_CREATE_USER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserAuthorizationData userAuthorizationData) throws InterruptedException {
        return given()
                .spec(getSpec())
                .body(userAuthorizationData)
                .when()
                .post(PATH_LOGIN_USER)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String token) throws InterruptedException {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .when()
                .delete(PATH_DELETE_USER)
                .then();
    }

    @Step("Изменение поля Name c авторизацией")
    public ValidatableResponse updateNameWithAuthorization(String token, UserName name) throws InterruptedException {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .and()
                .body(name)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }

    @Step("Изменение поля Name без авторизации")
    public ValidatableResponse updateNameWithoutAuthorization(UserName name) throws InterruptedException {
        return given()
                .spec(getSpec())
                .and()
                .body(name)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }

    @Step("Изменение поля Password с авторизацией")
    public ValidatableResponse updatePasswordWithAuthorization(String token, UserPassword password) throws InterruptedException {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .and()
                .body(password)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }

    @Step("Изменение поля Password без авторизации")
    public ValidatableResponse updatePasswordWithoutAuthorization(UserPassword password) throws InterruptedException {
        return given()
                .spec(getSpec())
                .and()
                .body(password)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }

    @Step("Изменение поля Email с авторизацией")
    public ValidatableResponse updateEmailWithAuthorization(String token, UserEmail email) throws InterruptedException {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .and()
                .body(email)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }

    @Step("Изменение поля Email без авторизации")
    public ValidatableResponse updateEmailWithoutAuthorization(UserEmail email) throws InterruptedException {
        return given()
                .spec(getSpec())
                .and()
                .body(email)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }
}
