import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.practicum.client.OrderClient;
import ru.practicum.step.OrderSteps;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class IngredientsTest {
    private final OrderSteps orderSteps = new OrderSteps(new OrderClient());

    @Test
    @DisplayName("Получить список ингредиентов")
    @Description("Тест проверяет получение списка всех доступных ингредиентов из системы. " +
            "Ожидается успешный ответ с кодом 200 (OK), содержащий непустой массив данных с информацией об ингредиентах.")
    public void getIngredients() {
        orderSteps.getIngridients()
                .assertThat()
                .statusCode(SC_OK)
                .body("data", notNullValue())
                .body("data", isA(ArrayList.class));
    }
}