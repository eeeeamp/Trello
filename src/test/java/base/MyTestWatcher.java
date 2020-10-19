package base;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import static io.restassured.RestAssured.given;

public class MyTestWatcher implements TestWatcher {

    String url;
    String path;
    String id;
    String key;
    String token;

    public MyTestWatcher(String url, String path, String id, String key, String token) {

        this.url = url;
        this.path = path;
        this.id = id;
        this.key = key;
        this.token = token;

    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {

        given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .delete(url + path + id)
                .then()
                .statusCode(HttpStatus.SC_OK);

    }


}
