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
import static org.junit.Assert.assertTrue;
import static test.Ingredients.*;
import test.model.*;
import test.service.*;

import java.util.List;

public class OrderGetInfoTest {

    private OrderService orderService = new OrderService();
    private User user = new User("modenovaelenadiplom" + System.currentTimeMillis() + "@gmail.com",
            "Elena" + System.currentTimeMillis(), "kristi" + System.currentTimeMillis());
    ;
    private UserService userService = new UserService();
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;
        accessToken = null;
    }

    @Test
    @DisplayName("Order get information with authorization")
    public void orderGetWithAuth() {
        Response createResponse = this.userService.createUser(this.user);
        this.accessToken = createResponse.then().extract().jsonPath().getString("accessToken");

        Order order = new Order(new String[]{FILE_ING, FLEURBULKA_ING});
        Response createResponse2 = this.orderService.createOrder(order, accessToken);

        Response getOrdersResponse = this.orderService.getOrders(accessToken);

        getOrdersResponse.then().assertThat().statusCode(200);
        getOrdersResponse.then().assertThat().body("success", equalTo(true));
        getOrdersResponse.then().assertThat().body("orders.ingredients", is(notNullValue()));
        List<Order> orders = getOrdersResponse.jsonPath().getList("orders", Order.class);
        assertTrue(orders.size() ==1);
    }
    @Test
    @DisplayName("Get order info without authorization")
    public void orderGetWithoutAuth() {

        Order order = new Order (new String[]{FILE_ING, FLEURBULKA_ING});

        Response createResponse = this.orderService.getOrders(null);

        createResponse.then().assertThat().statusCode(401);
        createResponse.then().assertThat().body("success", equalTo(false),
                "message", equalTo("You should be authorised"));

    }
    @After
    public void clear () {
        if (accessToken != null) {
            this.userService.deleteUser(accessToken);
        }
    }
}

