import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class ReqresTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    @DisplayName("Get user list")
    void getUserList() {

        String response =
                get("/api/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract().response().asString();

        System.out.println(response);
    }

    @Test
    @DisplayName("Get single user not found")
    void getSingleUserNotFound() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create user")
    void createUser() {
        given()
                .contentType(JSON)
                .body("{\"name\": \"ivan\"," +
                        "\"job\": \"driver\"}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("ivan"))
                .body("job", is("driver"));
    }

    @Test
    @DisplayName("Update user")
    void updateUser() {
        given()
                .contentType(JSON)
                .body("{\"name\": \"morpheus\"," +
                        "\"job\": \"zion resident\"}")
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Register successful")
    void registerSuccessful() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"pistol\"}")
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
}

