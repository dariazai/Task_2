package base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import task2.UserCreateHelpers;

public class BaseTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }
}
