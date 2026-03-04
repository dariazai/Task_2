package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserCreateHelpers {
    @Step("Создание нового курьера")
    public Response createNewUser() {
        return createNewUser(UserData.NAME, UserData.PASSWORD, UserData.EMAIL);
    }
    @Step("Создание нового курьера")
    public Response createNewUser(String email, String password, String name) {
        UserParameter userParameter = new UserParameter(email, password, name);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(userParameter)
                        .when()
                        .post("/api/auth/register");
        return response;
    }
}
