package test.service;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import test.model.User;
import static test.Urls.*;
import static io.restassured.RestAssured.given;
public class UserService {
    @Step("Create user via POST /api/auth/register")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_URL);

    }

    @Step("Login user via POST /api/auth/login")
    public Response loginUser(User user) {
        Response loginResponse =  given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .post(LOGIN_USER_URL);

      //  user.setaccessToken(loginResponse.then().extract().jsonPath().getString("accessToken"));
       // user.setrefreshToken(loginResponse.then().extract().jsonPath().getString("refreshToken"));

        return loginResponse;
    }

    @Step("Change User Data user via PATCH /api/auth/user")
    public Response updateUser(User user, String token) {
        if (token != null) {
           return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", token)
                    .and()
                    .body(user)
                    .patch(UPDATE_USER_URL);
        }
        else {
            return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(user)
                    .patch(UPDATE_USER_URL);
        }

    }

}
