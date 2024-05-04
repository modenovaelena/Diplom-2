package test.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static test.Urls.*;

import io.qameta.allure.Step;
import test.model.Order;

public class OrderService {
    @Step("Create order via POST /api/orders")
    public Response createOrder(Order order,String token) {
        if (token != null) {
            return given()
                .header("Content-type", "application/json")
                    .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_URL);
        }
        else {
            return given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post(CREATE_ORDER_URL);
        }

    }

    @Step("Get orders via GET /api/orders")
    public Response getOrders(token) {
        if (token != null) {
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", token)
                    .when()
                    .get(GET_ORDER_URL);
        } else {
            return given()
                    .header("Content-type", "application/json")
                    .when()
                    .get(GET_ORDER_URL);
        }

    }
}
