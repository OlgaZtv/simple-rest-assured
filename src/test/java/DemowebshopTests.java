import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;


public class DemowebshopTests {
    @BeforeAll
    @Test
    static void setup() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
    }

    @Test
    @DisplayName("Success authorization with login")
    void loginWithCookieTest() {
        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("Email", "zatulivetrova@gmail.com")
                        .formParam("Password", "123456")
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        open(baseURI);
        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        open(baseURI);
        $(".account").shouldHave(text("zatulivetrova@gmail.com"));
    }

    @Test
    @DisplayName("Add product to Cart with Cookie")
    void addToCartWithCookieTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("addtocart_13.EnteredQuantity=1")
                        .cookie("Nop.customer=29c3c4d1-060e-44e3-891c-9c9545434793; ARRAffinity=55622bac41413dfac968dd8f036553a9415557909fd0cd3244e7e0e656e4adc8; __RequestVerificationToken=0-JPwHJ4O-14n9JpEkr5bqEnzbBrIb1-uAhGoUS_gYiRN2fwY2HHF2nol7RGt258E-xgtcKTc0gULjx5YvZAg4zQcfcgBX5GOeEm2lLkfwg1; ASP.NET_SessionId=2k2mfqdeqxibmxfqw5vqd2ej; NOPCOMMERCE.AUTH=10CC62FE6EDE2BBADED9B153FF63CF6DA33648735BA0E33527F3D7C0D12CC6B051C35BA3E03F5ACB6FFDB1A8C13365C83EF40C522D91FBE575BBF29923E64C765E7E6C98A4A81D4874F766D78CBCAEA5A5FC55452C88A8CA526EC293E162ADA4764ED22E46381D26654517580F07AC4A321769E6B6BD5B3D07263A31139555EF3F3F787A8B2CD48B8785EF6094FB3BE41EC11626D939A9CAA416F8D722A5F1298FD9AE91D17FE966A438D2641DEE5DF0; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=13&RecentlyViewedProductIds=31; nop.CompareProducts=CompareProductIds=13&CompareProductIds=31")
                        .when()
                        .post("addproducttocart/details/13/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
//                        .body("updatetopcartsectionhtml", is("(15)"))
                        .extract().response();

        System.out.println("Response: " + response.path("updatetopcartsectionhtml"));

    }
}
