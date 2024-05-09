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
public class OrderCreationTest {

    private OrderService orderService = new OrderService();


    private User user = new User("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
            "Elena"+ System.currentTimeMillis(), "kristi"+ System.currentTimeMillis());;
    private UserService userService = new UserService();
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;
        accessToken =null;
    }

    @Test
    @DisplayName("Order Creation with authorization")
    public void orderCreationWithAuth() {
        Response createResponse = this.userService.createUser(this.user);
        this.accessToken = createResponse.then().extract().jsonPath().getString("accessToken");

        Order order = new Order (new String[]{FILE_ING, FLEURBULKA_ING});

        Response createResponse2 = this.orderService.createOrder(order,accessToken);

        createResponse2.then().assertThat().statusCode(200);
        createResponse2.then().assertThat().body("success", equalTo(true));

        createResponse2.then().assertThat().body("order.ingredients", is(notNullValue()));


    }

    @Test
    @DisplayName("Order Creation without authorization")
    public void orderCreationWithoutAuth() {

        Order order = new Order (new String[]{FILE_ING, FLEURBULKA_ING});

        Response createResponse = this.orderService.createOrder(order,null);

        createResponse.then().assertThat().statusCode(200);
        createResponse.then().assertThat().body("success", equalTo(true));
        createResponse.then().assertThat().body("order.ingredients", nullValue());
    }


    @Test
    @DisplayName("Order Creation with several ingredients")
    public void orderCreationWithIng() {
        Response createResponse = this.userService.createUser(this.user);
        this.accessToken = createResponse.then().extract().jsonPath().getString("accessToken");
        Order order = new Order (new String[]{FILE_ING, FLEURBULKA_ING,MYASO_ING});

        Response createResponse2 = this.orderService.createOrder(order,accessToken);

        createResponse2.then().assertThat().statusCode(200);
        createResponse2.then().assertThat().body("success", equalTo(true));
        createResponse2.then().assertThat().body("order.ingredients", is(notNullValue()));

    }

    @Test
    @DisplayName("Order Creation without ingredients")
    public void orderCreationWithoutIng() {
        Response createResponse = this.userService.createUser(this.user);
        this.accessToken = createResponse.then().extract().jsonPath().getString("accessToken");
        Order order = new Order (new String[]{});

        Response createResponse2 = this.orderService.createOrder(order,accessToken);

        createResponse2.then().assertThat().statusCode(400);
        createResponse2.then().assertThat().body("success", equalTo(false),
                "message", equalTo("Ingredient ids must be provided"));

    }
    @Test
    @DisplayName("Order Creation with invalid ingredient")
    public void orderCreationInvalidIng() {
        Response createResponse = this.userService.createUser(this.user);
        this.accessToken = createResponse.then().extract().jsonPath().getString("accessToken");

        Order order = new Order (new String[]{"6de"});

        Response createResponse2 = this.orderService.createOrder(order,accessToken);

        createResponse2.then().assertThat().statusCode(500);
        //createResponse2.then().assertThat().body("success", equalTo(false),
       //         "message", equalTo("Internal Server Error"));

    }

    @After
    public void clear () {
        if (accessToken != null) {
            this.userService.deleteUser(accessToken);
        }
    }
}



