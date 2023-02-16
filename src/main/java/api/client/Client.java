package api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    public static final int REQUEST_DELAY = 500;
    protected RequestSpecification getSpec() throws InterruptedException {
        Thread.sleep(REQUEST_DELAY);

        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }


}
