package user;

import base.BaseData;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateOrderTest extends BaseData {
    static OrderHelpers newOrder;

    @BeforeAll
    public static void setUp() {
        newOrder = new OrderHelpers();
    }

    @Description("Создание заказа неавторизованным пользователем")
    @Test
    public void createOrderWithIngTest() {
        Response response = newOrder.getIngredients();

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("data", notNullValue());

        String firstIngredient = response.jsonPath().getString("data[0]._id");
        String nameIngredient = response.jsonPath().getString("data[0].name");
        Response responseNewOrder= newOrder.createOrder(firstIngredient);
        responseNewOrder.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", equalTo(nameIngredient))
                .body("order.number", notNullValue());
    }
}
