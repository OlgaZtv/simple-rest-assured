package tests;

import filters.CustomLogFilter;
import io.qameta.allure.restassured.AllureRestAssured;
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
import static filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;


public class DemowebshopTests {
    public static String Response;
    public static String Email = "zatulivetrova@gmail.com";
    public static String Password = "123456";
    public static String AddToCartCookie = "Nop.customer=29c3c4d1-060e-44e3-891c-9c9545434793; ARRAffinity=55622bac41413dfac968dd8f036553a9415557909fd0cd3244e7e0e656e4adc8; __RequestVerificationToken=0-JPwHJ4O-14n9JpEkr5bqEnzbBrIb1-uAhGoUS_gYiRN2fwY2HHF2nol7RGt258E-xgtcKTc0gULjx5YvZAg4zQcfcgBX5GOeEm2lLkfwg1; ASP.NET_SessionId=2k2mfqdeqxibmxfqw5vqd2ej; NOPCOMMERCE.AUTH=10CC62FE6EDE2BBADED9B153FF63CF6DA33648735BA0E33527F3D7C0D12CC6B051C35BA3E03F5ACB6FFDB1A8C13365C83EF40C522D91FBE575BBF29923E64C765E7E6C98A4A81D4874F766D78CBCAEA5A5FC55452C88A8CA526EC293E162ADA4764ED22E46381D26654517580F07AC4A321769E6B6BD5B3D07263A31139555EF3F3F787A8B2CD48B8785EF6094FB3BE41EC11626D939A9CAA416F8D722A5F1298FD9AE91D17FE966A438D2641DEE5DF0; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=13&RecentlyViewedProductIds=31; nop.CompareProducts=CompareProductIds=13&CompareProductIds=31";

    @BeforeAll
    @Test
    static void setup() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
    }

    @Test
    @DisplayName("Success authorization with login (API + UI)")
    void loginWithCookieTest() {
        step("Получить cookie через api и установить его в браузере", () -> {
            String authorizationCookie =
                    given()
                            .filter(customLogFilter().withCustomTemplates())
                            .log().uri()
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", Email)
                            .formParam("Password", Password)
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Открыть главную страницу сайта, потому что cookie можно установить при открытии сайта", () ->
                    open(baseURI));
            step("Установить cookie в браузер", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
            step("Открыть главную страницу сайта", () ->
                    open(baseURI));
            step("Проверить электронную почту", () ->
                    $(".account").shouldHave(text(Email)));
        });
    }

    @Test
    @DisplayName("Add product to Cart with Cookie (API + UI)")
    void addToCartWithCookieTest() {
        step("Добавить товар  корзину", () ->
                Response =
                        given()
                                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                                .body("addtocart_13.EnteredQuantity=1")
                                .cookie(AddToCartCookie)
                                .when()
                                .post("addproducttocart/details/13/1")
                                .then()
                                .statusCode(200)
                                .body("success", is(true))
                                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                                .extract()
                                .path("updatetopcartsectionhtml"));

        step("Открыть главную страницу сайта", () ->
                open(baseURI));
        step("Количество товара = " + Response, () ->
                $(".cart-qty").shouldHave(text(Response)));

    }
}
