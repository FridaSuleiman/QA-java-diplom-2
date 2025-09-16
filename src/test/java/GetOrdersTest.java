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
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class GetOrdersTest {
    private OrderSteps orderSteps;
    private UserSteps userSteps;
    private String name;
    private String password;
    private String email;
    private String accessToken;

    @Before
    public void setUp() {
        orderSteps = new OrderSteps(new OrderClient());
        userSteps = new UserSteps(new UserClient());
        name = RandomStringUtils.randomAlphabetic(10);
        password = RandomStringUtils.randomAlphabetic(10);
        email = RandomStringUtils.randomAlphabetic(10) + "@mail.test";
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
    @DisplayName("Получить заказ, сделанный без авторизации в списке всех заказов")
    @Description("Тест проверяет, что заказ, созданный без авторизации пользователя, не отображается в общем списке всех заказов. " +
            "Ожидается, что система корректно фильтрует заказы по наличию авторизации.")
    public void createOrderWithoutAuthAndGetItInAllOrdersListNotFound() {
        ValidatableResponse ingredientsInfo = orderSteps.getIngridients();
        List<String> ingredientsList = orderSteps.chooseIngridients(ingredientsInfo, 2);
        String orderId = orderSteps.createOrderWithoutAuth(ingredientsList)
                .extract()
                .path("order.number")
                .toString();

        assertFalse("Заказ, сделанный без авторизации не должен отображаться в общем списке",
                orderSteps.checkOrderInAllOrdersList(orderId));
    }

    @Test
    @DisplayName("Получить заказ, сделанный c авторизацией в списке всех заказов")
    @Description("Тест проверяет, что заказ, созданный с авторизацией пользователя, отображается в общем списке всех заказов. " +
            "Ожидается, что система корректно сохраняет и отображает заказы авторизованных пользователей.")
    public void createOrderWithAuthAndGetItInAllOrdersListSuccessfullyFound() {
        userSteps.createUser(email, password, name);
        accessToken = userSteps.getUserToken(email, password);
        ValidatableResponse ingredientsInfo = orderSteps.getIngridients();
        List<String> ingredientsList = orderSteps.chooseIngridients(ingredientsInfo, 2);

        String orderId = orderSteps.createOrderWithAuth(ingredientsList, accessToken)
                .extract()
                .path("order.number")
                .toString();

        assertTrue("Заказ, сделанный c авторизацией должен отображаться в общем списке",
                orderSteps.checkOrderInAllOrdersList(orderId));
    }

    @Test
    @DisplayName("Получить заказ, сделанный c авторизацией в списке заказов пользователя")
    @Description("Тест проверяет, что заказ, созданный с авторизацией пользователя, отображается в персональном списке заказов этого пользователя. " +
            "Ожидается, что система корректно связывает заказы с учетными записями пользователей.")
    public void createOrderWithAuthAndGetItInUserOrdersListSuccessfullyFound() {
        userSteps.createUser(email, password, name);
        accessToken = userSteps.getUserToken(email, password);
        ValidatableResponse ingredientsInfo = orderSteps.getIngridients();
        List<String> ingredientsList = orderSteps.chooseIngridients(ingredientsInfo, 2);

        String orderId = orderSteps.createOrderWithAuth(ingredientsList, accessToken)
                .extract()
                .path("order.number")
                .toString();

        assertTrue("Заказ, сделанный с авторизацией должен отображаться в списке заказов пользователя",
                orderSteps.checkOrderInUserOrdersList(orderId, accessToken));
    }

    @Test
    @DisplayName("Попытка получить список заказов пользователя без авторизации")
    @Description("Тест проверяет попытку получения списка заказов пользователя без предоставления токена авторизации. " +
            "Ожидается ответ с кодом ошибки 401 (Unauthorized), так как доступ к персональным данным требует аутентификации.")
    public void getUserOrdersWithoutAuth() {
        orderSteps.getUserOrdersWithoutAuth()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }
}