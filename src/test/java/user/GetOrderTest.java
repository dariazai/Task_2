package user;

import base.BaseData;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.*;

public class GetOrderTest extends BaseData {
    static OrderHelpers newOrder;
    static UserCreateHelpers createUser;
    private boolean userCreated;

    @BeforeAll
    public static void setUp() {
        newOrder = new OrderHelpers();
    }

    @Description("Получение списка заказов авторизованным пользователем")
    @Test
    public void getOrderListAuthUserTest() {
        createUser = new UserCreateHelpers();
        createUser.createNewUser()
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        String accessToken = createUser.authUser()
                .jsonPath()
                .getString("accessToken");

        Response response = newOrder.getIngredients();

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("data", notNullValue());

        String firstIngredient = response.jsonPath().getString("data[0]._id");
        Response responseNewOrder = newOrder.createOrder(firstIngredient, accessToken);
        responseNewOrder.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
        newOrder.getOrdersUser(accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
           }

    @Description("Получение списка заказов авторизованным пользователем")
    @Test
    public void getOrderListNoAuthUserTest() {
        createUser = new UserCreateHelpers();
        createUser.createNewUser()
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        String accessToken = createUser.authUser()
                .jsonPath()
                .getString("accessToken");

        Response response = newOrder.getIngredients();

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("data", notNullValue());

        String firstIngredient = response.jsonPath().getString("data[0]._id");
        Response responseNewOrder = newOrder.createOrder(firstIngredient, accessToken);
        responseNewOrder.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
        newOrder.getOrdersUser(null)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
           }

    @AfterEach
    public void afterEach() {
        createUser.deleteUser();
    }
}