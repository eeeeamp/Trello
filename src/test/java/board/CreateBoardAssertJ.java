package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;


public class CreateBoardAssertJ extends BaseTest {

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

        assertThat(jsonResponse.getString("name")).isEqualTo(boardName);

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

        assertThat(jsonResponse.getString("name")).isEqualTo(boardName);
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

        assertThat(idList).isEmpty();

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


        assertThat(jsonResponse.getString("name")).isEqualTo(boardName);


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

        List<String> nameList = jsonResponseList.getList("name");

        assertThat(nameList).hasSize(3).contains("To Do", "Doing", "Done");

        deleteResource(BOARDS, boardId);

    }

}
