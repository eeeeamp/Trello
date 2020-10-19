package organisation;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.*;


public class WithoutMandatoryFields extends BaseTest {

    @BeforeEach
    protected void beforeEach() {

        orgName = f.funnyName().name();

    }

    @Test
    public void createOrganizationWithoutKey() {

        given()
                .queryParam("token", TOKEN)
                .queryParam("displayName", orgName)
                .contentType(JSON)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_UNAUTHORIZED);

    }

    @Test
    public void createOrganizationWithoutToken() {

        given()
                .queryParam("key", KEY_NUMBER)
                .queryParam("displayName", orgName)
                .contentType(JSON)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_UNAUTHORIZED);

    }

    @Test
    public void createOrganizationWithoutDisplayName() {

        given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_BAD_REQUEST);

    }

    @Test
    public void createOrganizationWithoutContentTypeDefined() {

        given()
                .queryParam("key", KEY_NUMBER)
                .queryParam("token", TOKEN)
                .queryParam("displaYName", orgName)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_UNSUPPORTED_MEDIA_TYPE);
    }

}
