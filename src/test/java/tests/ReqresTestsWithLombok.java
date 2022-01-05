package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.CreateUserRequest;
import tests.lombok.CreateUserResponse;
import tests.lombok.RegisterUser;
import tests.lombok.Users;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static tests.Specs.request;
import static tests.Specs.responseSpec;


public class ReqresTestsWithLombok {


    @Test
    @DisplayName("Get user list")
    void getUserListwithLombok() {
        Users data = given().spec(request)
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
    @DisplayName("Create user lombok")
    void createUserLombok() {
        CreateUserRequest newCreateuser = new CreateUserRequest("ivan", "driver");

        CreateUserResponse response = given().spec(request)
                .body(newCreateuser)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().as(CreateUserResponse.class);

        assertEquals(newCreateuser.getName(), response.getName());
        assertEquals(newCreateuser.getJob(), response.getJob());
        assertFalse(response.getId().isEmpty());
        assertFalse(response.getCreatedAt().isEmpty());

    }

    @Test
    @DisplayName("Update user lombok")
    void updateUserLombok() {
        CreateUserRequest newCreateuser = new CreateUserRequest("morpheus", "zion resident");

        CreateUserResponse response = given().spec(request)
                .body(newCreateuser)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(CreateUserResponse.class);

        assertEquals(newCreateuser.getName(), response.getName());
        assertEquals(newCreateuser.getJob(), response.getJob());
        // assertNotNull(response.getUpdateAt());

    }

    @Test
    @DisplayName("Register successful Lombok")
    void registerSuccessfulLombok() {
        RegisterUser registrationData = new RegisterUser();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        CreateUserResponse response = given().spec(request)
                .body(registrationData)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .extract().as(CreateUserResponse.class);


        assertEquals("4", response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
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

