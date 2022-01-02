package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.models.UserData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.Specs.request;
import static tests.Specs.responseSpec;


public class ReqresTestsWithModels {


    @Test
    @DisplayName("Get user list")
    void getUserList() {
        given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .log().body();
    }

    @Test
    @DisplayName("Get single user not found")
    void getSingleUserNotFound() {
        given()
                .spec(request)
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create user")
    void createUser() {
        given()
                .spec(request)
                .body("{\"name\": \"ivan\"," +
                        "\"job\": \"driver\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", is("ivan"))
                .body("job", is("driver"));
    }

    @Test
    @DisplayName("Update user")
    void updateUser() {
        given()
                .spec(request)
                .body("{\"name\": \"morpheus\"," +
                        "\"job\": \"zion resident\"}")
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Register successful")
    void registerSuccessful() {
        given()
                .spec(request)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"pistol\"}")
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Single User")
    void singleUser() {
        given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body();
    }

    @Test
    @DisplayName("Single User with model")
    void singleUserWithModel() {
        UserData data = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(UserData.class);
        assertEquals(2, data.getData().getId());
    }
}

