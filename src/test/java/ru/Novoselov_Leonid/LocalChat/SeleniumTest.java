package ru.Novoselov_Leonid.LocalChat;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.Novoselov_Leonid.LocalChat.entity.User;
import ru.Novoselov_Leonid.LocalChat.service.UserService;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthSeleniumTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;
    private String testLogin;
    private final String testPassword = "1234";

    @BeforeAll
    static void setupDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        testLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
        userService.createUser(testLogin, testPassword, "test");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        User createdUser = userService.findByLogin(testLogin);
        if (createdUser != null) {
            userService.deleteById(createdUser.getId());
        }
    }

    @Test
    void loginAndLogout_Success() {
        driver.get(baseUrl + "/login");
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        usernameInput.sendKeys(testLogin);
        passwordInput.sendKeys(testPassword);
        submitButton.click();
        wait.until(ExpectedConditions.urlContains("/custom/chat-rooms/view/list"));
        assertTrue(driver.getCurrentUrl().contains("/custom/chat-rooms/view/list"),
                "После входа пользователь должен быть перенаправлен на список комнат");

        driver.get(baseUrl + "/logout");
        WebElement logoutConfirmButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        logoutConfirmButton.click();
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "После выхода пользователь должен вернуться на страницу входа");
    }
}
