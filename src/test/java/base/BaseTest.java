package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static final String KEY_NUMBER = "d3ae3de7eb86b03f96cc1228661b1fd2";
    protected static final String TOKEN = "f6865822249d1fe2cb72a3d0f1fe9655f6daa098cffabee4bc04e154ac3ccae6";
    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String BOARDS = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARDS = "cards";
    protected static final String ORGANIZATIONS = "organizations";
    protected static Faker f;

    private static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;
    protected static String orgName;

    @BeforeAll
    protected static void beforeAll() {

        f = new Faker();
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY_NUMBER);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        //wzorzec projektowy - jak uzywamy builera ZAWSZe musimy zrobic na koniec build
        reqSpec = reqBuilder.build();
    }

    protected void deleteResource(String path, String id) {

        given()
                .queryParam("key", KEY_NUMBER)
                .queryParam("token", TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_URL + path + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

}



