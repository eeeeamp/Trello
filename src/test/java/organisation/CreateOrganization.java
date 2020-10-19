package organisation;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.*;

public class CreateOrganization extends BaseTest {

    private static String organizationId;

    @BeforeEach
    protected void beforeEach() {

        orgName = f.funnyName().name();

    }

    @AfterEach
    public void afterEach() {

        deleteResource(ORGANIZATIONS, organizationId);

    }

    @Test
    public void createOrganization() {

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

    @Test
    public void createOrganizationWithAllAvailableFields() {

        String description = f.witcher().quote();
        String website = f.internet().url();

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .queryParam("desc", description)
                .queryParam("name", "2organization_36789")
                .queryParam("website", "http://" + website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertThat(jsonResponse.getString("name")).isEqualTo("2organization_36789");
        assertThat(jsonResponse.getString("displayName")).isEqualTo(orgName);
        assertThat(jsonResponse.getString("desc")).isEqualTo(description);
        assertThat(jsonResponse.getString("website")).isEqualTo("http://" + website);

        organizationId = jsonResponse.get("id");

    }

    @Test
    public void createOrganizationWithWebsiteStartsWithHttps() {

        String description = f.witcher().quote();
        String website = f.internet().url();

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", orgName)
                .queryParam("website", "https://" + website)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertThat(jsonResponse.getString("displayName")).isEqualTo(orgName);
        assertThat(jsonResponse.getString("website")).isEqualTo("https://" + website);

        organizationId = jsonResponse.get("id");

    }

}
