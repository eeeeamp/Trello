package organisation;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvalidParameters extends BaseTest {

    private static String organizationId;

//    MyTestWatcher myTestWatcher = new MyTestWatcher(BASE_URL, ORGANIZATIONS, organizationId, KEY_NUMBER, TOKEN);

    @BeforeEach
    protected void beforeEach() {

        orgName = f.funnyName().name();

    }

    @Test
    @Order(1)
    public void nameOfOrganizationTooShorts() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .queryParam("name", "AA")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        organizationId = jsonResponse.get("id");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST);

    }

    @Test
    @Order(2)
    public void deleteOrganization() {

        deleteResource(ORGANIZATIONS, organizationId);

    }

    @Test
    @Order(3)
    public void nameOfOrganizationWithSpaces() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .queryParam("name", "Organization Name With Spaces")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        organizationId = jsonResponse.get("id");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST);

    }

    @Test
    @Order(4)
    public void deleteOrganization1() {

        deleteResource(ORGANIZATIONS, organizationId);

    }

    @Test
    @Order(5)
    public void nameOfOrganizationWithSpecialCharacters() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .queryParam("name", "&*^test$#")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        organizationId = jsonResponse.get("id");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST);

    }

    @Test
    @Order(6)
    public void deleteOrganization2() {

        deleteResource(ORGANIZATIONS, organizationId);

    }

    @Test
    @Order(7)
    public void websiteUrlWithoutHttp() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .queryParam("website", "www.test.pl")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        organizationId = jsonResponse.get("id");

        Assertions.assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST);

    }

    @Test
    @Order(8)
    public void deleteOrganization3() {

        deleteResource(ORGANIZATIONS, organizationId);

    }



}
