import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.client.OrderClient;
import ru.practicum.step.OrderSteps;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderWithoutAuthTest {
    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps(new OrderClient());
    }

    @Test
    @DisplayName("Создать заказ без авторизации с валидными ингредиентами")
    @Description("Тест проверяет создание заказа без авторизации пользователя с валидным списком ингредиентов. " +
            "Ожидается успешное создание заказа с кодом ответа 200 и флагом success=true.")
    public void createOrderValidIngredientsWithoutAuth() {
        ValidatableResponse ingredientsInfo = orderSteps.getIngridients();
        List<String> ingredientsList = orderSteps.chooseIngridients(ingredientsInfo, 5);

        orderSteps.createOrderWithoutAuth(ingredientsList)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }
}