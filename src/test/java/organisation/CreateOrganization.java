package organisation;

import base.BaseTest;
import base.MyTestWatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MyTestWatcher.class)
public class CreateOrganization extends BaseTest {

    private static String organizationId;

    private static Stream<Arguments> createOrganizationData() {

        return Stream.of(
                Arguments.of("This is a test Organization", "This is a description", "pixelteam", "https://www.test.pl"),
                Arguments.of("This is a test Organization", "This is a description", "pix", "http://www.test.pl"),
                Arguments.of("This is a test Organization", "This is a description", "pixel_team", "http://www.test.pl"),
                Arguments.of("This is a test Organization", "This is a description", "12pixelteam12", "http://www.test.pl"));

    }

    @AfterEach
    public void afterEach() {

       if (organizationId != null && !organizationId.trim().isEmpty())
       {
           MyTestWatcher.path = ORGANIZATIONS;
           MyTestWatcher.id = organizationId;
       }

    }

    @Test
    @Order(1)
    public void createOrganization() {

        orgName = f.funnyName().name();

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertThat(jsonResponse.getString("displayName")).isEqualTo(orgName);

        organizationId = jsonResponse.get("id");

    }

    @DisplayName("Create an organization with all and valid data")
    @ParameterizedTest(name = "Display Name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationData")
    @Order(2)
    public void createOrganizationWithAllAvailableFields(String displayName, String desc, String name, String website) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertThat(jsonResponse.getString("displayName")).isEqualTo(displayName);
        assertThat(jsonResponse.getString("desc")).isEqualTo(desc);
        assertThat(jsonResponse.getString("website")).isEqualTo(website);

        organizationId = jsonResponse.get("id");

    }

    @DisplayName("Create an organization with all and valid data")
    @ParameterizedTest(name = "Display Name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationData")
    @Order(3)
    public void tryToCreateOrganizationWithExistingName(String displayName, String desc, String name, String website) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        organizationId = jsonResponse.get("id");

        Assertions.assertThat(response.statusCode()).isEqualTo(SC_BAD_REQUEST);

    }

}
