import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreationTest extends BaseUserTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Тест проверяет успешное создание пользователя с валидными данными.")
    public void createUserValidParams() {
        userSteps.createUser(email, password, name)
                .assertThat()
                .statusCode(SC_OK)
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание дубликата пользователя")
    @Description("Тест проверяет попытку создания пользователя с уже существующими данными.")
    public void createDuplicateUser() {
        userSteps.createUser(email, password, name);
        userSteps.createUser(email, password, name)
                .assertThat()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без обязательных параметров")
    @Description("Тест проверяет попытку создания пользователя без указания обязательных полей.")
    public void createUserWithoutRequiredParams() {
        // Проверка отсутствия пароля
        userSteps.createUserWithoutPassword(email, name)
                .assertThat()
                .statusCode(SC_FORBIDDEN);

        // Проверка отсутствия email
        userSteps.createUserWithoutEmail(password, name)
                .assertThat()
                .statusCode(SC_FORBIDDEN);
    }
}