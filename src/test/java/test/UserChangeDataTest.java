package test;
import io.qameta.allure.Step;
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
public class UserChangeDataTest {
    private UserService userService = new UserService();
    private String accessToken ;
    private User user = new User("modenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
            "Elena"+ System.currentTimeMillis(), "kristi"+ System.currentTimeMillis());;
    @Before
    public void setUp() {
        RestAssured.baseURI = Urls.BASE_URI;

        Response createResponse = this.userService.createUser(this.user);
        this.accessToken= createResponse.then().extract().jsonPath().getString("accessToken");

    }


    @Test
    @DisplayName("Change data with auth ")
    public void changeDataWithAuthEmail() {
        Response updateResponse = this.userService.updateUser(
                new User(
                        "newmodenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
                       null,
                        null
                ),
                accessToken
        );
        updateResponse.then().assertThat().statusCode(200);
        updateResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Change data with name ")
    public void changeDataWithAuthName() {
        Response updateResponse = this.userService.updateUser(
                new User(
                        null,
                        null,
                        "newkristi"+ System.currentTimeMillis()
                ),
                accessToken
        );
        updateResponse.then().assertThat().statusCode(200);
        updateResponse.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Change data - password ")
    public void changeDataWithAuthPassword() {
        Response updateResponse = this.userService.updateUser(
                new User(
                        null,
                        "NewElena"+ System.currentTimeMillis(),
                        null
                ),
                accessToken
        );
        updateResponse.then().assertThat().statusCode(200);
        updateResponse.then().assertThat().body("success", equalTo(true));
    }


    @Test
    @DisplayName("Change data with  the same email ")
    public void changeDataWithAuthSameEmail() {

        User user2 = new User("Denis" + System.currentTimeMillis() +"@gmail.com",
                "Denis"+ System.currentTimeMillis(), "kristi"+ System.currentTimeMillis());;
         this.userService.createUser(user2);

        Response updateResponse = this.userService.updateUser(
                new User(
                        user2.getEmail(),
                        null,
                        null
                ),
                accessToken
        );
        updateResponse.then().assertThat().statusCode(403);
        updateResponse.then().assertThat().body("success", equalTo(false),
                "message", equalTo("User with such email already exists"));
    }


    @Test
    @DisplayName("Change data without auth")
    public void changeDataWithoutAuth() {
        Response updateResponse = this.userService.updateUser(
                new User(
                        "newmodenovaelenadiplom" + System.currentTimeMillis() +"@gmail.com",
                        null,
                        null
                ),
                null
        );
        updateResponse.then().assertThat().statusCode(401);
        updateResponse.then().assertThat().body("success", equalTo(false),
                "message", equalTo("You should be authorised"));
        ;
    }

}
