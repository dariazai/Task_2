package user;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateHelpers {
    @Step("Создание нового пользователя")
    public Response createNewUser() {
        return createNewUser(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
    }

    @Step("Создание нового пользователя")
    public Response createNewUser(String email, String password, String name) {
        UserParameter userParameter = new UserParameter(email, password, name);
        Response response =
                given()
                        .contentType("application/json")
                        .body(userParameter)
                        .when()
                        .post("/api/auth/register");
        return response;
    }

    @Step("Авторизация нового пользователя")
    public Response authUser(String email, String password, String name) {
        UserParameter userParameter = new UserParameter(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userParameter)
                        .when()
                        .post("/api/auth/login");
        return response;
    }

    @Step("Авторизация нового пользователя")
    public Response authUser() {
        return authUser(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
    }



    @Step("Удаление пользователя")
    public Response deleteUser() {
        String accessToken = authUser().jsonPath()
                .getString("accessToken");
        Response response =
                given()
                        .header("Authorization", accessToken)
                        .and()
                        .when()
                        .delete("/api/auth/user");
        response.then()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true))
                .body("message",equalTo( "User successfully removed"));

        return response;
    }
    @Step("Логин пользователя")
    public Response loginUser(String email, String password, String name) {
        UserParameter userParameter = new UserParameter(email, password, name);
        Response response =
                given()
                        .contentType("application/json")
                        .body(userParameter)
                        .when()
                        .post("/api/auth/login");
        return response;
    }
}
