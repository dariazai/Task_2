package user;

import base.BaseData;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.then;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthUserTest extends BaseData {
    static UserCreateHelpers createUser;


    @BeforeAll
    public static void setUp() {
        createUser = new UserCreateHelpers();
    }

    @Description("Авторизация пользователя. Позитивная проверка")
    @Test
    public void authUserTest() {
        createUser.createNewUser()
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        createUser.authUser()
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(UserData.EMAIL))
                .body("user.name", equalTo(UserData.NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());;

    }

    @Description("Попытка авторизации пользователя с неполными данными")
    @ParameterizedTest
    @MethodSource("provider")
    public void authUserNotAllFieldsTransmittedTest(String email, String password, String name) {
        createUser.createNewUser();
        createUser.authUser(email, password, name)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false),
                        "message", equalTo("email or password are incorrect"));

    }

    private static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(UserData.EMAIL, null, UserData.NAME),
                Arguments.of(null, UserData.PASSWORD, UserData.NAME));
    }

    @Description("Попытка авторизации пользователя с отсутствующим необязательным полем name")
    @Test
    public void authUserNotNameFieldsTransmittedTest() {
        createUser.createNewUser()
                .then()
                .statusCode(SC_OK);
        createUser.authUser(UserData.EMAIL,UserData.PASSWORD,null)
                .then()
                .statusCode(SC_OK);

    }

    @AfterEach
    public void afterEach() {
            createUser.deleteUser();
    }
}
