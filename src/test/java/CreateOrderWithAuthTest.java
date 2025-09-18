import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.client.OrderClient;
import ru.practicum.client.UserClient;
import ru.practicum.step.OrderSteps;
import ru.practicum.step.UserSteps;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderWithAuthTest {
    private OrderSteps orderSteps;
    private UserSteps userSteps;
    private String password;
    private String email;
    private String accessToken;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps(new OrderClient());
        userSteps = new UserSteps(new UserClient());
        String name = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        email = RandomStringUtils.randomAlphabetic(10) + "@mail.test";

        // Создание пользователя и получение токена в @Before
        userSteps.createUser(email, password, name);
        accessToken = userSteps.getUserToken(email, password);
    }

    @After
    public void tearDown() {
        try {
            userSteps.deleteUser(email, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Создать заказ с авторизацией с валидными ингредиентами")
    @Description("Тест проверяет создание заказа с авторизацией пользователя с валидным списком ингредиентов. " +
            "Ожидается успешное создание заказа с кодом ответа 200 и флагом success=true.")
    public void createOrderValidIngredientsWithAuth() {
        ValidatableResponse ingredientsInfo = orderSteps.getIngridients();
        List<String> ingredientsList = orderSteps.chooseIngridients(ingredientsInfo, 5);

        orderSteps.createOrderWithAuth(ingredientsList, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создать заказ с пустым списком ингредиентов")
    @Description("Тест проверяет создание заказа с пустым списком ингредиентов. " +
            "Ожидается ответ с кодом ошибки 400 (Bad Request).")
    public void createOrderNoneIngredients() {
        orderSteps.createOrderWithAuth(new ArrayList<>(), accessToken)
                .assertThat()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создать заказ с некорректным списком ингредиентов")
    @Description("Тест проверяет создание заказа с некорректным (несуществующим) ингредиентом. " +
            "Ожидается ответ с кодом ошибки 500 (Internal Server Error).")
    public void createOrderInvalidIngredients() {
        ArrayList<String> invalidIngredient = new ArrayList<>();
        invalidIngredient.add("invalidIngredient");

        orderSteps.createOrderWithAuth(invalidIngredient, accessToken)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}