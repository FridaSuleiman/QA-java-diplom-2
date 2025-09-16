import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

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
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход пользователя с некорректными данными")
    @Description("Тест проверяет попытку аутентификации с неверными учетными данными.")
    public void loginUserInvalidData() {
        // Неверный пароль
        userSteps.loginUser(email, "некорректный пароль")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);

        // Неверный email
        userSteps.loginUser("некорректный@mail.mail", password)
                .statusCode(SC_UNAUTHORIZED);
    }
}