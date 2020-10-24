package base;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import java.util.Properties;

import static base.ReadFromPropertiesFile.*;
import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String BOARDS = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARDS = "cards";
    protected static final String ORGANIZATIONS = "organizations";
    protected static Faker f;

    private static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;
    protected static String orgName;

    protected static String KEY_NUMBER;
    protected static String TOKEN;

    @BeforeAll
    protected static void beforeAll() throws Exception {

        Properties properties = readPropertiesFile("credentials.properties");
        KEY_NUMBER = properties.getProperty("key");
        TOKEN = properties.getProperty("token");

        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY_NUMBER);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);
        reqSpec = reqBuilder.build();

        f = new Faker();
    }

    public static void deleteResource(String path, String id) {

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



