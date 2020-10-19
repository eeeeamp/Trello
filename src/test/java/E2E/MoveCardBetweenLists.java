package E2E;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoveCardBetweenLists extends BaseTest {

    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String cardId;

    @Test
    @Order(1)
    public void createABoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My e2e board")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertThat(jsonResponse.getString("name")).isEqualTo("My e2e board");

        boardId = jsonResponse.get("id");

    }

    @Test
    @Order(2)
    public void createFirstList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "First List")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        Assertions.assertThat(jsonResponse.getString("idBoard")).isEqualTo(boardId);
        Assertions.assertThat(jsonResponse.getString("name")).isEqualTo("First List");

        firstListId = jsonResponse.get("id");

    }

    @Test
    @Order(3)
    public void createSecondList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Second List")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        Assertions.assertThat(jsonResponse.getString("idBoard")).isEqualTo(boardId);
        Assertions.assertThat(jsonResponse.getString("name")).isEqualTo("Second List");

        secondListId = jsonResponse.get("id");

    }

    @Test
    @Order(4)
    public void createCardInFirstList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My E2E card")
                .queryParam("idList", firstListId)
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        Assertions.assertThat(jsonResponse.getString("name")).isEqualTo("My E2E card");
        Assertions.assertThat(jsonResponse.getString("idList")).isEqualTo(firstListId);

        cardId = jsonResponse.get("id");

    }

    @Test
    @Order(5)
    public void moveCardToSecondList() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", secondListId)
                .put(BASE_URL + CARDS + "/" + cardId)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        Assertions.assertThat(jsonResponse.getString("name")).isEqualTo("My E2E card");
        Assertions.assertThat(jsonResponse.getString("idList")).isEqualTo(secondListId);

    }

    @Test
    @Order(6)
    public void deleteBoard() {

        deleteResource(BOARDS, boardId);

    }


}
