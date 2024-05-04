package test;


import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static test.Ingredients.*;
import test.model.*;
import test.service.*;
public class OrderGetInfoTest {

    private OrderService orderService = new OrderService();
    private User user = new User("modenovaelenadiplom" + System.currentTimeMillis() + "@gmail.com",
            "Elena" + System.currentTimeMillis(), "kristi" + System.currentTimeMillis());
    ;
    private UserService userService = new UserService();

    @Before
    public void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;

    }

    @Test
    @DisplayName("Order get information with authorization")
    public void orderGetWithAuth() {
        Response createResponse = this.userService.createUser(this.user);
        String accessToken = createResponse.then().extract().jsonPath().getString("accessToken");

        Order order = new Order(new String[]{FILE_ING, FLEURBULKA_ING});
        Response createResponse2 = this.orderService.createOrder(order, accessToken);

        Response createResponse3 = this.userService.createUser(this.user);

        createResponse2.then().assertThat().statusCode(200);
        createResponse2.then().assertThat().body("success", equalTo(true));

        createResponse2.then().assertThat().body("order.ingredients", is(notNullValue()));


    }
    @Test
    @DisplayName("Get order info without authorization")
    public void orderGetWithoutAuth() {

        Order order = new Order (new String[]{FILE_ING, FLEURBULKA_ING});

        Response createResponse = this.orderService.createOrder(order,null);

        createResponse.then().assertThat().statusCode(200);
        createResponse.then().assertThat().body("success", equalTo(true));
        createResponse.then().assertThat().body("order.ingredients", nullValue());
    }

}
