package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.Users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tests.Specs.request;
import static tests.Specs.responseSpec;


public class ReqresTestsWithLombok {


    @Test
    @DisplayName("Get user list")
    void getUserListwithLombok() {
        Users data =
                given()
                        .spec(request)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .log().body()
                        .extract().as(Users.class);

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
        String body = "{\"name\": \"ivan\"," +
                "\"job\": \"driver\"}";
        given()
                .spec(request)
                .body(body)
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
        String body = "{\"name\": \"morpheus\"," +
                "\"job\": \"zion resident\"}";

        given()
                .spec(request)
                .body(body)
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
        String body = "{\"email\": \"eve.holt@reqres.in\"," +
                "\"password\": \"pistol\"}";
        given()
                .spec(request)
                .body(body)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Single User with lombok")
    void singleUserWithLombok() {
        int expectedId = 2;
        Users userResponse = given().spec(request)
                .when()
                .pathParam("id", "2")
                .get("/users/{id}")
                .then()
                .spec(responseSpec)
                .extract().as(Users.class);

        assertEquals(expectedId, userResponse.getId());
    }

    @Test
    void checkIdAndEmailOfFeaturedUser() {
        Users userResponse = given().spec(request)
                .when()
                .pathParam("id", "2")
                .get("/users/{id}")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().jsonPath().getObject("data", Users.class);

        assertEquals(2, userResponse.getId());
        assertTrue(userResponse.getEmail().endsWith("@reqres.in"));
    }


    @Test
    public void checkEmailUsingGroovy() {
        // @formatter:off
        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
        // @formatter:on
    }
}

