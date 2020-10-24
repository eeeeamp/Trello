package board;

import base.BaseTest;
import base.MyTestWatcher;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MyTestWatcher.class)
public class CreateBoard extends BaseTest {

    private String boardName;
    private static String boardId;

    @BeforeEach
    public void beforeEach() {

        boardName = f.book().title();

    }

    @AfterEach
    public void afterEach() {

        if (boardId != null && !boardId.trim().isEmpty()) {
            MyTestWatcher.path = BOARDS;
            MyTestWatcher.id = boardId;
        }
    }

    @Test
    public void createABoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", boardName)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        boardId = jsonResponse.get("id");

        assertEquals(SC_OK, response.statusCode());
        assertEquals(boardName, jsonResponse.get("name"));


    }

    @Test
    public void createABoardWithEmptyName() {

        given()
                .spec(reqSpec)
                .queryParam("name", "")
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(SC_BAD_REQUEST);

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
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        boardId = jsonResponse.get("id");

        assertEquals(SC_OK, response.statusCode());
        assertEquals(boardName, jsonResponse.get("name"));

        //to check if the board containing lists
        Response responseLists = given()
                .spec(reqSpec)
                .pathParam("id", boardId)
                .when()
                .get(BASE_URL + BOARDS + "/{id}/" + LISTS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath jsonResponseList = responseLists.jsonPath();

        List<String> idList = jsonResponseList.getList("id");

        assertTrue(idList.isEmpty());

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
                .extract()
                .response();

        JsonPath jsonResponse = response.jsonPath();

        boardId = jsonResponse.get("id");

        assertEquals(SC_OK, response.statusCode());
        assertEquals(boardName, jsonResponse.get("name"));

        //to check if the board containing lists
        Response responseLists = given()
                .spec(reqSpec)
                .pathParam("id", boardId)
                .when()
                .get(BASE_URL + BOARDS + "/{id}/" + LISTS)
                .then()
                .extract()
                .response();

        JsonPath jsonResponseList = responseLists.jsonPath();

        List<String> nameList = jsonResponseList.getList("name");

        assertEquals(SC_OK, response.statusCode());
        assertEquals("To Do", nameList.get(0));
        assertEquals("Doing", nameList.get(1));
        assertEquals("Done", nameList.get(2));

    }

}
