import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserManagementTest extends BaseUserTest {
    private String accessToken;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        // Создание пользователя перед каждым тестом управления
        userSteps.createUser(email, password, name)
                .assertThat()
                .statusCode(SC_OK);
        accessToken = userSteps.getUserToken(email, password);
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    @Description("Тест проверяет возможность изменения данных пользователя.")
    public void updateUserData() {
        // Обновление имени
        userSteps.updateName(email, password, "новое имя")
                .assertThat()
                .statusCode(SC_OK)
                .body("user.name", equalTo("новое имя"));

        // Обновление email
        String newEmail = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) + "@mail.test";
        userSteps.updateEmail(email, password, newEmail)
                .assertThat()
                .statusCode(SC_OK)
                .body("user.email", equalTo(newEmail.toLowerCase()));
    }

    @Test
    @DisplayName("Обновление данных без авторизации")
    @Description("Тест проверяет попытку изменения данных пользователя без авторизации.")
    public void updateUserWithoutAuth() {
        String newEmail = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) + "@mail.test";
        userSteps.updateDataWithoutAuth(newEmail, "Новое имя")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Обновление почты на существующую")
    @Description("Тест проверяет попытку изменения email на уже занятый.")
    public void updateUserEmailOnExistEmail() {
        String newUserEmail = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(10) + "@mail.test";
        userSteps.createUser(newUserEmail, password, name);
        userSteps.updateEmail(newUserEmail, password, email)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User with such email already exists"));
    }
}