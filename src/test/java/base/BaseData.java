package base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseData {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }
}
