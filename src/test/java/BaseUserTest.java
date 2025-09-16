import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.practicum.client.UserClient;
import ru.practicum.step.UserSteps;

public class BaseUserTest {
    protected UserSteps userSteps;
    protected String name;
    protected String password;
    protected String email;

    @Before
    public void setUp() {
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
}