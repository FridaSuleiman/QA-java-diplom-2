import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

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
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists")); // Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Тест проверяет попытку создания пользователя без указания пароля.")
    public void createUserWithoutPassword() {
        userSteps.createUserWithoutPassword(email, name)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields")); // Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Тест проверяет попытку создания пользователя без указания email.")
    public void createUserWithoutEmail() {
        userSteps.createUserWithoutEmail(password, name)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields")); // Проверка сообщения об ошибке
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Тест проверяет попытку создания пользователя без указания имени.")
    public void createUserWithoutName() {
        userSteps.createUserWithoutName(email, password)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields")); // Проверка сообщения об ошибке
    }
}