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
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MyTestWatcher.class)
public class CreateBoardAssertJ extends BaseTest {

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

        assertThat(response.statusCode()).isEqualTo(SC_OK);
        assertThat(jsonResponse.getString("name")).isEqualTo(boardName);

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

        assertThat(response.statusCode()).isEqualTo(SC_OK);
        assertThat(jsonResponse.getString("name")).isEqualTo(boardName);

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

        List<String> idList = jsonResponseList.getList("id");

        assertThat(response.statusCode()).isEqualTo(SC_OK);
        assertThat(idList).isEmpty();

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

        assertThat(response.statusCode()).isEqualTo(SC_OK);
        assertThat(jsonResponse.getString("name")).isEqualTo(boardName);

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

        assertThat(response.statusCode()).isEqualTo(SC_OK);
        assertThat(nameList).hasSize(3).contains("To Do", "Doing", "Done");


    }

}
