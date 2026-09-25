package joaquinpiedracueva.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import joaquinpiedracueva.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver).open();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void standardUserCanLogIn() {
        loginPage.loginAs("standard_user", "secret_sauce");

        // The inventory page renders after the URL changes, so wait for its heading
        var title = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='title']")));

        assertTrue(driver.getCurrentUrl().endsWith("/inventory.html"));
        assertEquals("Products", title.getText());
    }

    @Test
    void lockedOutUserSeesError() {
        loginPage.loginAs("locked_out_user", "secret_sauce");

        assertEquals("Epic sadface: Sorry, this user has been locked out.", loginPage.getErrorMessage());
    }

    @Test
    void wrongPasswordShowsError() {
        loginPage.loginAs("standard_user", "wrong_password");

        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessage());
    }
}
