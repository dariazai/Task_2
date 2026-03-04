package user;

import base.BaseData;
import io.qameta.allure.Description;
import org.hamcrest.Matchers;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateUserTest extends BaseData {
    static UserCreateHelpers createUser;
   // private boolean courierCreated;

    @BeforeAll
    public static void setUp() {
        createUser = new UserCreateHelpers();
    }

    @Description("Создание пользователя . Позитивная проверка")
    @Test
    public void createNewCourier() {

        createUser.createNewUser()
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(UserData.EMAIL))
                .body("user.name", equalTo(UserData.NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
       // courierCreated = true;
    }

    @Description("Попытка создания пользователя с неполными данными")
    @ParameterizedTest
    @MethodSource("provider")
    public void createUserNotAllFieldsTransmittedTest(String login, String password, String email) {
        createUser.createNewUser(login, password, email)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false),
                        "message", equalTo("Email, password and name are required fields"));
       // courierCreated = false;
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(UserData.NAME, null, UserData.EMAIL),
                Arguments.of(null, UserData.PASSWORD, UserData.EMAIL),
                Arguments.of(UserData.NAME, UserData.PASSWORD, null));
    }

  /*  @Description("Попытка создания курьера с существующим логином")
    @Test
    public void creatingCourierWithExistingLoginTest() {
        createCourier.createNewCourier()
                .then()
                .statusCode(SC_CREATED)
                .body("ok", Matchers.equalTo(true));

        createCourier.createNewCourier()
                .then()
                .statusCode(SC_CONFLICT)
                .body("code", equalTo(409),
                        "message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courierCreated = true;
    }

    @AfterEach
    public void afterEach() {
        if (courierCreated) {
            createCourier.deleteCourier();
        }
    }*/
}
