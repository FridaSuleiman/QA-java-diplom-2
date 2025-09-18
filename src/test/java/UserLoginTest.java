import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends BaseUserTest {

    @Before
    @Override
    public void setUp() {
        super.setUp();
        // Создание пользователя перед каждым тестом авторизации
        userSteps.createUser(email, password, name)
                .assertThat()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Корректный вход пользователя")
    @Description("Тест проверяет успешную аутентификацию пользователя с валидными учетными данными.")
    public void loginUserValidData() {
        userSteps.loginUser(email, password)
                .assertThat()
                .statusCode(SC_OK)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Вход пользователя с некорректным email")
    @Description("Тест проверяет попытку аутентификации с неверным email.")
    public void loginUserInvalidEmail() {
        userSteps.loginUser("некорректный@mail.mail", password)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход пользователя с некорректным паролем")
    @Description("Тест проверяет попытку аутентификации с неверным паролем.")
    public void loginUserInvalidPassword() {
        userSteps.loginUser(email, "некорректный пароль")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход пользователя без email")
    @Description("Тест проверяет попытку аутентификации без указания email.")
    public void loginUserWithoutEmail() {
        userSteps.loginUser("", password)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход пользователя без пароля")
    @Description("Тест проверяет попытку аутентификации без указания пароля.")
    public void loginUserWithoutPassword() {
        userSteps.loginUser(email, "")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}