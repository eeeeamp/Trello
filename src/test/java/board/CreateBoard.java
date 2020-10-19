package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateBoard  extends BaseTest {

    private String boardName;
    private static String boardId;

    @BeforeEach
    public void beforeEach() {

        boardName = f.book().title();

    }

    @Test
    public void createABoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", boardName)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertEquals(boardName, jsonResponse.get("name"));

        boardId = jsonResponse.get("id");

        deleteResource(BOARDS, boardId);

    }

    @Test
    public void createABoardWithEmptyName() {

        given()
                .spec(reqSpec)
                .queryParam("name", "")
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void createABoardWithoutLists() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", boardName)
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertEquals(boardName, jsonResponse.get("name"));


        boardId = jsonResponse.get("id");

        //to check if the board containing lists
        Response responseLists = given()
                .spec(reqSpec)
                .pathParam("id", boardId)
                .when()
                .get(BASE_URL + BOARDS + "/{id}/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonResponseList = responseLists.jsonPath();

        List<String> idList = jsonResponseList.getList("id");

        assertTrue(idList.isEmpty());

        deleteResource(BOARDS, boardId);

    }

    @Test
    public void createABoardWithLists() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", boardName)
                .queryParam("defaultLists", true)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        assertEquals(boardName, jsonResponse.get("name"));

        boardId = jsonResponse.get("id");

        //to check if the board containing lists
        Response responseLists = given()
                .spec(reqSpec)
                .pathParam("id", boardId)
                .when()
                .get(BASE_URL + BOARDS + "/{id}/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath jsonResponseList = responseLists.jsonPath();

        List<String> idList = jsonResponseList.getList("id");
        List<String> nameList = jsonResponseList.getList("name");

        assertEquals("To Do", nameList.get(0));
        assertEquals("Doing", nameList.get(1));
        assertEquals("Done", nameList.get(2));

        deleteResource(BOARDS, boardId);

    }

}
