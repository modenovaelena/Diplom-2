package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.equalTo;

import test.model.*;
import test.service.*;
public class UserCreationTest {
    private User user;
    private UserService userService = new UserService();
    @Before
    public void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;
    }

    @Test
    @DisplayName("Check if it is possible to create user")
    public void userCreation() {
        this.user  = new User ("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
                "Elena"+ System.currentTimeMillis(), "kristi"+ System.currentTimeMillis());

        Response createResponse = this.userService.createUser(this.user);
        createResponse.then().assertThat().statusCode(200);
        createResponse.then().assertThat().body("success",equalTo(true));

       // this.userService.loginUser(this.user);
    }

    @Test
    @DisplayName("Check that it is not possible to create two similar couriers twice")
    public void duplicateUserCreation() {
        this.user  = new User ("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
                "Elena"+ System.currentTimeMillis(), "kristi"+ System.currentTimeMillis());


        // create first user
        Response createResponse1 = this.userService.createUser(this.user);
        createResponse1.then().assertThat().statusCode(200);

        // attempt to create second user with the same data
        Response createResponse2 = this.userService.createUser(this.user);
        createResponse2.then().assertThat().statusCode(403);
        createResponse2.then().assertThat().body("success",equalTo(false),
                "message",equalTo("User already exists"));


     //   this.userService.loginUser(this.user);

    }
    @Test
    @DisplayName("Check that user creation failed in case of missed email parameter")
    public void UserCreationWithoutEmail() {
        this.user = new User(null,
                "Elena" + System.currentTimeMillis(), "kristi" + System.currentTimeMillis());
        Response createResponse = this.userService.createUser(this.user);
        createResponse.then().assertThat().statusCode(403);
        createResponse.then().assertThat().body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Check that user creation failed in case of missed password parameter")
    public void UserCreationWithoutPassword() {
        this.user = new User("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
                null, "kristi"+ System.currentTimeMillis());
        Response createResponse = this.userService.createUser(this.user);
        createResponse.then().assertThat().statusCode(403);
        createResponse.then().assertThat().body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Check that user creation failed in case of missed name parameter")
    public void UserCreationWithoutName() {
        this.user = new User("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
                "Elena"+ System.currentTimeMillis(), null);
        Response createResponse = this.userService.createUser(this.user);
        createResponse.then().assertThat().statusCode(403);
        createResponse.then().assertThat().body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }

    }
