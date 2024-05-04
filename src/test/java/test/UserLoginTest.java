package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import test.model.*;
import test.service.*;



public class UserLoginTest {

    private UserService userService = new UserService();

    private User user = new User("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
            "Elena"+ System.currentTimeMillis(), "kristi"+ System.currentTimeMillis());;

    @Before
    public void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;
        this.userService.createUser(this.user);
    }

    @Test
    @DisplayName("Check if it is possible to login user under existing user ")
    public void existingUserLogin() {
        Response loginResponse = this.userService.loginUser(
                new User(
                        this.user.getEmail(),
                        this.user.getPassword()
                )
        );
        loginResponse.then().assertThat().statusCode(200);
        loginResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check if it is possible to login user with wrong password ")
    public void userLoginWrongPassword() {
        Response loginResponse = this.userService.loginUser(
                new User(
                        this.user.getEmail(),
                        "privet"
                )
        );
        loginResponse.then().assertThat().statusCode(401);
        loginResponse.then().assertThat().body("success",equalTo(false),
                "message",equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Check if it is possible to login user with wrong login ")
    public void userLoginWrongLogin() {
        Response loginResponse = this.userService.loginUser(
                new User(
                        "privet",
                        this.user.getPassword()
                )
        );
        loginResponse.then().assertThat().statusCode(401);
        loginResponse.then().assertThat().body("success",equalTo(false),
                "message",equalTo("email or password are incorrect"));
    }

}
