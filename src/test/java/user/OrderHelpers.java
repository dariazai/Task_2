package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderHelpers {
    OrderParameter orderParameter= new OrderParameter();
    @Step("Получение данных о доступных ингридиентов")
    public Response getIngredients() {
        Response response =
                given()
                        .contentType("application/json")
                        .when()
                        .get("/api/ingredients");
        return response;
    }
    @Step("Создание заказа")
    public Response createOrder(String ingredients) {
        OrderParameter orderParameter=new OrderParameter(ingredients);

        Response response =
                given()
                        .contentType("application/json")
                        .body(orderParameter)
                        .when()
                        .post("api/orders");
        return response;
    }
}
