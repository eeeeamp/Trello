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
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MyTestWatcher.class)
public class InvalidParameters extends BaseTest {

    private static String organizationId;

    @AfterEach
    public void afterEach() {

        if (organizationId != null && !organizationId.trim().isEmpty()) {
            MyTestWatcher.path = ORGANIZATIONS;
            MyTestWatcher.id = organizationId;
        }

    }

    @BeforeEach
    protected void beforeEach() {

        orgName = f.funnyName().name();

    }

    private static Stream<Arguments> createOrganizationData() {

        return Stream.of(
                //invalid name
                Arguments.of("Test Organization", "My Organization", "AA", "http://www.test.pl"),
                Arguments.of("Test Organization", "My Organization", "", "http://www.test.pl"),
                Arguments.of("Test Organization", "My Organization", "UPPERCASES_TEST", "http://www.test.pl"),
                Arguments.of("Test Organization", "My Organization", "$specialchar%*&", "http://www.test.pl"),

                //invalid website
                Arguments.of("Test Organization", "My Organization", "test_emilka_098_test", "www.test.pl"),
                Arguments.of("Test Organization", "My Organization", "test_emilka_654_test", "10"),
                Arguments.of("Test Organization", "My Organization", "test_emilka_475_test", ""),

                //empty display name
                Arguments.of("", "My Organization", "test_emilka_123_test", "https://www.test.pl")
        );
    }

    @DisplayName("Try to create an organization with invalid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationData")
    public void tryToCreateOrganizationWithInvalidParams(String displayName, String desc, String name, String website) {

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

        Assertions.assertThat(response.getStatusCode()).isEqualTo(SC_BAD_REQUEST);

    }
}

