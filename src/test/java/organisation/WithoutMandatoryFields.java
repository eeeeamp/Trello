package organisation;

import base.BaseTest;
import base.MyTestWatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MyTestWatcher.class)
public class WithoutMandatoryFields extends BaseTest {

    private static String organizationId;

    @BeforeEach
    protected void beforeEach() {

        orgName = f.funnyName().name();

    }

    @AfterEach
    public void afterEach() {

        if (organizationId != null && !organizationId.trim().isEmpty()) {
            MyTestWatcher.path = ORGANIZATIONS;
            MyTestWatcher.id = organizationId;
        }

    }

    @Test
    public void createOrganizationWithoutKey() {

        Response response = given()
                .queryParam("token", TOKEN)
                .queryParam("displayName", orgName)
                .contentType(JSON)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        if (!response.getBody().asString().contains("invalid key")) {

            JsonPath jsonResponse = response.jsonPath();

            organizationId = jsonResponse.getString("id");

        }

        assertThat(response.statusCode()).isEqualTo(SC_UNAUTHORIZED);

    }

    @Test
    public void createOrganizationWithoutToken() {

        Response response = given()
                .queryParam("key", KEY_NUMBER)
                .queryParam("displayName", orgName)
                .contentType(JSON)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        if (!response.getBody().asString().contains("unauthorized organization.")) {

            JsonPath jsonResponse = response.jsonPath();

            organizationId = jsonResponse.getString("id");

        }
        assertThat(response.statusCode()).isEqualTo(SC_UNAUTHORIZED);

    }

    @Test
    public void createOrganizationWithoutDisplayName() {

        Response response = given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        organizationId = jsonResponse.getString("id");

        assertThat(response.statusCode()).isEqualTo(SC_BAD_REQUEST);

    }

    @Test
    public void createOrganizationWithoutContentTypeDefined() {

        given()
                .queryParam("key", KEY_NUMBER)
                .queryParam("token", TOKEN)
                .queryParam("displayName", orgName)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_UNSUPPORTED_MEDIA_TYPE);

    }

}
